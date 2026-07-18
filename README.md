# Hotbar Cycle

A Fabric client-side mod that lets you cycle inventory rows into your hotbar
using a modifier key + scroll.

Inspired by [Shifty Hotbar](https://modrinth.com/mod/shifty-hotbar) by Kir-Antipov
(abandoned since 2023, last available for 1.20.4) — this is a focused hotbar swap mod
for modern versions, not a fork. There are no current plans to implement additional
functionality from Shifty Hotbar.

![](https://raw.githubusercontent.com/Vicenthresh/hotbar-cycle/master/hotbar-cycle.gif)

## Behavior

Hold `Left Alt` (rebindable) and scroll while playing (no screen open):

| Input | Action |
|-------|--------|
| Modifier + scroll up | Rows shift upward — row 1 becomes the hotbar |
| Modifier + scroll down | Rows shift downward — row 3 becomes the hotbar |

The 4 inventory rows (hotbar + 3 rows, 36 slots total) rotate as a wheel.
Items stay in their columns — slot 0 always maps to the leftmost column.

## Compatibility

Minecraft `>=1.21.1` through `1.21.4`. Client-only — works on any server
(vanilla or modded) without the server needing this mod.

## How it works

The mixin intercepts `Mouse.onMouseScroll` and sends 27 `SWAP` packets per
scroll tick (9 columns × 3 row swaps), simulating number-key hotbar swaps.
The server processes them as normal inventory interactions.

```
Hold Alt + scroll
  → MouseMixin intercepts onMouseScroll
  → 27 clickSlot(SWAP) calls rotate all rows
  → ci.cancel() prevents vanilla slot selection
```

## Project structure

```
├── build.gradle                     ← Fabric Loom, dependencies
├── gradle.properties                ← MC version, mappings, mod info
├── settings.gradle                  ← project name
└── src/main/
    ├── java/com/hotbarcycle/
    │   ├── HotbarCycle.java         ← ClientModInitializer, key binding
    │   └── mixin/
    │       ├── KeyBindingAccessor.java ← exposes KeyBinding.boundKey
    │       └── MouseMixin.java         ← intercepts mouse scroll
    └── resources/
        ├── fabric.mod.json          ← mod metadata
        ├── hotbar-cycle.mixins.json ← mixin configuration
        └── assets/hotbar-cycle/
            └── lang/en_us.json      ← controls menu labels
```

## Build

```bash
./gradlew build
```

Output: `build/libs/hotbar-cycle-1.0.0.jar`

## Contributing

Pull requests for newer Minecraft versions are welcome.

1. Branch off `master` as `mc-<version>` (e.g., `mc-26.2`)
2. Update `minecraft_version` and `yarn_mappings` in `gradle.properties`
3. Fix any compile errors, rebuild with `./gradlew build`
4. Open a PR back to the repo

Licensed under MIT — forks and ports are explicitly allowed.
