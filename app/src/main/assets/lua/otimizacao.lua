-- Roblox Boost Optimization Profile
-- Lua declarative optimization profile

local profile = {}

-- Optimization levels: basic, medium, extreme
profile.level = "basic"

-- Basic optimization
profile.basic = {
    forceStop = true,
    compilation = "speed-profile"
}

-- Medium optimization
profile.medium = {
    compilation = "speed-profile",
    speed = true
}

-- Extreme optimization
profile.extreme = {
    compilation = "speed",
    bgDexoptJob = true,
    trimCaches = "limited"
}

return profile
