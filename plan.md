# Shift Scroll — Plan

A Fabric client-side mod that lets you cycle inventory rows into your hotbar using
a modifier key + scroll, inspired by Stardew Valley's hotbar system.

## Behavior

| Input | Action |
|-------|--------|
| Hold `Left Alt` + scroll up | Shift inventory rows upward — the row above the hotbar becomes the hotbar |
| Hold `Left Alt` + scroll down | Shift inventory rows downward — the hotbar becomes the row above |

The 4 inventory rows (hotbar + 3 rows, 36 slots total) rotate like a wheel.
When row 3 shifts upward, it wraps into the hotbar; when the hotbar shifts
downward, it wraps into row 3.

## What You'll Learn

1. **Fabric project structure** — build.gradle, gradle.properties, fabric.mod.json
2. **Mod entry points** — `ClientModInitializer` (runs once on game start)
3. **Key bindings** — registering configurable keys via Fabric API's `KeyBindingHelper`
4. **Mixins** — injecting code into vanilla Minecraft classes:
   - `@Mixin` — tells Mixin which class to modify
   - `@Inject` — adds code at a specific point in a method
   - `@ModifyVariable` — changes a method parameter
   - `@Shadow` — lets you call private methods from the target class
5. **Player inventory manipulation** — how Minecraft stores items in the 36-slot array
6. **Client tick events** — running code on the render thread

## Implementation Plan (Step by Step)

### Step 1 — Project Scaffolding

Create the standard Fabric Loom project from the Fabric template.
Files needed:
- `build.gradle` — build config (Loom plugin, dependencies, Minecraft version)
- `gradle.properties` — version variables
- `settings.gradle` — project name
- `src/main/resources/fabric.mod.json` — mod metadata (id, name, entry points, mixins)

Run `./gradlew build` to verify the empty mod loads in a dev environment.

### Step 2 — Key Binding

Register a modifier key binding (`Left Alt` by default) using Fabric API's
`KeyBindingHelper.registerKeyBinding()`.

Where: `ShiftScroll.onInitializeClient()` calls our `ModKeyBindings.register()`.
Class: `ModKeyBindings` — holds the static `KeyBinding` instance.

The key binding shows up in Minecraft's controls menu under a "Shift Scroll"
category so the player can rebind it.

### Step 3 — Row Shifting Logic

Create a utility class `RowShifter` with `shiftRow(PlayerInventory, boolean up)`:

How shifting works:
- Minecraft stores the entire inventory (hotbar + 3 rows) in one flat array of
  `ItemStack` at `PlayerInventory.main` (indices 0–35).
- Hotbar is slots 0–8, row 1 is 9–17, row 2 is 18–26, row 3 is 27–35.
- To shift UP: for each column (0–8), rotate items downward through
  slots [col, col+9, col+18, col+27].
- To shift DOWN: rotate items upward instead.

This keeps items in their columns — your sword in slot 0 stays in the leftmost
column regardless of the row.

### Step 4 — Mixin Into PlayerInventory

The vanilla method `PlayerInventory.scrollInHotbar(double scrollAmount)` fires
every time the player scrolls while the inventory is open. We use a mixin to:

1. Check if our modifier key is held.
2. If YES: shift rows instead of changing the selected slot.
3. If NO: pass through to vanilla behavior.

Mixins use `@Inject` with `at = @At("HEAD")` and `cancellable = true` to
intercept the method. Calling `ci.cancel()` prevents the original code from
running (effectively replacing it with ours).

Target method in yarn mappings:
- `net.minecraft.entity.player.PlayerInventory.scrollInHotbar(D)V`

### Step 5 — Prevent Default Scroll

When the modifier key is held and we shift rows, we also need to prevent the
scroll from changing the selected slot. We use `@Inject` with `cancel = true`
on the `scrollInHotbar` method.

### Step 6 — Optional: HUD Indicator

If time permits, render a small text showing "Row 1/4" above the hotbar when
scrolling with the modifier held. This requires a second mixin into
`InGameHud` (or `HandledScreen`) and a `ClientTickEvents.END_CLIENT_TICK`
callback to track a fading timer.

### Step 7 — Configuration

Add a simple config file (`config/shift-scroll.json`) to let the player:
- Change the modifier key (already done via controls menu)
- Invert scroll direction
- Set how many inventory rows to cycle (default: all 4)

Use `SimpleConfig` (Fabric API) or manual JSON parsing.

### Step 8 — Testing

- Launch a dev client via `./gradlew runClient`
- Open your inventory, hold Left Alt, and scroll
- Verify items shift correctly across all 4 rows
- Verify the key binding shows in Controls menu
- Verify the mod doesn't interfere with normal hotbar scrolling

## Key Minecraft Classes

| Class | Purpose |
|-------|---------|
| `PlayerInventory` | Holds the 36-slot inventory array. `main[0-8]` = hotbar, `main[9-35]` = 3 rows. Method `scrollInHotbar` changes the selected slot. |
| `MinecraftClient` | Singleton for the running game. `player` = local player, `options` = settings. |
| `KeyBinding` | Represents a bindable key. Fabric's `KeyBindingHelper` makes registration easy. |
| `ClientTickEvents` | Callbacks that fire every frame. Useful for tracking key-press state over time. |

## Project Folder Structure

```
shift-scroll/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradlew / gradlew.bat
├── plan.md                          ← this file
├── src/main/
│   ├── java/com/shiftscroll/
│   │   ├── ShiftScroll.java         ← ClientModInitializer entry point
│   │   ├── ModKeyBindings.java      ← key binding registration
│   │   ├── RowShifter.java          ← row shifting logic
│   │   └── mixin/
│   │       └── PlayerInventoryMixin.java
│   └── resources/
│       ├── fabric.mod.json
│       ├── shift-scroll.mixins.json
│       └── assets/shift-scroll/
│           └── lang/en_us.json
└── plan.md
```
