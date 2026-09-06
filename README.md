# Roblox Boost

App Android de otimização do Roblox usando Shizuku sem exigir root.

## Níveis
- Básico: force-stop + compilação speed-profile.
- Médio: compilação speed-profile + speed.
- Extremo: compilação speed + bg-dexopt-job + trim-caches limitado.

## Lua
O projeto inclui `app/src/main/assets/lua/otimizacao.lua`, usado como perfil declarativo da otimização. O app não modifica arquivos internos do Roblox.

## Requisitos
- Android 8.0+
- Shizuku instalado e iniciado.
- No Android 11+, o Shizuku pode ser iniciado pelo Wireless Debugging.
- O usuário precisa autorizar o Roblox Boost dentro do Shizuku.

A API do Shizuku usada é a oficial `dev.rikka.shizuku:api:13.1.5` + `provider:13.1.5`.

## Observação
O ganho de FPS não é garantido: o app aplica operações de compilação/cache permitidas pelo sistema. Não existe comando universal que aumente FPS em todos os aparelhos.
