package com.briansantos.robloxboost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var log: TextView
    private lateinit var progress: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val requestCode = 9001
    private val robloxPackage = "com.roblox.client"

    private val permissionListener = Shizuku.OnRequestPermissionResultListener { code, result ->
        if (code == requestCode) updateShizukuStatus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.statusText)
        log = findViewById(R.id.logText)
        progress = findViewById(R.id.progress)

        Shizuku.addRequestPermissionResultListener(permissionListener)

        findViewById<Button>(R.id.shizukuButton).setOnClickListener {
            if (!Shizuku.pingBinder()) {
                log.text = "Shizuku não está iniciado. Abra o Shizuku e inicie o serviço primeiro."
                return@setOnClickListener
            }
            if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(requestCode)
            } else {
                updateShizukuStatus()
            }
        }

        findViewById<Button>(R.id.optimizeButton).setOnClickListener {
            optimize()
        }

        updateShizukuStatus()
    }

    private fun updateShizukuStatus() {
        val connected = Shizuku.pingBinder()
        val allowed = connected &&
                Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        status.text = when {
            allowed -> "● Shizuku conectado e autorizado — sem root"
            connected -> "● Shizuku conectado — autorização necessária"
            else -> "○ Shizuku não conectado"
        }
    }

    private fun optimize() {
        if (!Shizuku.pingBinder() ||
            Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            log.text = "Conecte e autorize o Shizuku antes de otimizar."
            return
        }

        val level = when {
            findViewById<RadioButton>(R.id.extreme).isChecked -> "extremo"
            findViewById<RadioButton>(R.id.medium).isChecked -> "medio"
            else -> "basico"
        }

        progress.visibility = ProgressBar.VISIBLE
        findViewById<Button>(R.id.optimizeButton).isEnabled = false
        log.text = "Iniciando otimização $level..."

        Thread {
            val lines = mutableListOf<String>()
            try {
                val lua = assets.open("lua/otimizacao.lua").bufferedReader().use { it.readText() }
                lines += "Lua: perfil carregado (${lua.length} bytes)"
                lines += "Nível: $level"

                val commands = OptimizationProfile.commandsFor(level, robloxPackage)
                for ((i, cmd) in commands.withIndex()) {
                    lines += "[$i/${commands.size}] $cmd"
                    val result = runShizuku(cmd)
                    if (result.output.isNotBlank()) lines += result.output.trim()
                    if (result.exitCode != 0) lines += "Aviso: comando retornou ${result.exitCode}"
                    publish(lines.joinToString("\n"))
                }

                lines += "Concluído. Abra o Roblox e teste o desempenho."
            } catch (e: Exception) {
                lines += "Erro: ${e.message ?: e.javaClass.simpleName}"
            }

            publish(lines.joinToString("\n"))
            handler.post {
                progress.visibility = ProgressBar.GONE
                findViewById<Button>(R.id.optimizeButton).isEnabled = true
            }
        }.start()
    }

    private data class Result(val output: String, val exitCode: Int)

    private fun runShizuku(command: String): Result {
        val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        val error = BufferedReader(InputStreamReader(process.errorStream)).use { it.readText() }
        val code = process.waitFor()
        return Result((output + if (error.isNotBlank()) "\n$error" else "").trim(), code)
    }

    private fun publish(text: String) {
        handler.post { log.text = text }
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(permissionListener)
        super.onDestroy()
    }
}
