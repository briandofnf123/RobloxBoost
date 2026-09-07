package com.briansantos.robloxboost

object OptimizationProfile {
    fun commandsFor(level: String, pkg: String): List<String> {
        val common = listOf(
            "cmd activity force-stop $pkg",
            "cmd package compile -m speed-profile -f $pkg"
        )

        return when (level) {
            "medio" -> common + listOf(
                "cmd package compile -m speed -f $pkg"
            )
            "extremo" -> common + listOf(
                "cmd package compile -m speed -f $pkg",
                "cmd package bg-dexopt-job",
                "pm trim-caches 256M"
            )
            else -> common
        }
    }
}
