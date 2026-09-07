package com.briansantos.robloxboost

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

class OptimizationProfile {
    private val globals = JsePlatform.standardGlobals()

    fun loadProfile(luaScript: String): LuaValue {
        return globals.load(luaScript).call()
    }

    fun getOptimizationLevel(profile: LuaValue): String {
        return profile.get("level").tojstring()
    }
}
