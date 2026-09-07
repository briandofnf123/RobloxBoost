-- Roblox Boost: perfil Lua local.
-- Este arquivo descreve o perfil de otimização usado pelo app.
-- Não altera arquivos internos do Roblox nem tenta modificar o jogo.

local profiles = {
    basico = {
        force_stop = true,
        compile = "speed-profile",
        trim_cache_mb = 0
    },

    medio = {
        force_stop = true,
        compile = "speed",
        trim_cache_mb = 0
    },

    extremo = {
        force_stop = true,
        compile = "speed",
        dexopt_job = true,
        trim_cache_mb = 256
    }
}

function get_profile(name)
    return profiles[name] or profiles.basico
end
