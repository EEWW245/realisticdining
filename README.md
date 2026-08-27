# Realistic Dining

A mod that greatly enhances the Minecraft dining experience: from chopping, stir-frying and farming, to full first-person eating animations (press **U** to drink/eat snacks, **T** to eat rice), a snack display stand for placing 3D models, a vending machine, and customizable 3D food animation extensions.

> This mod is AI-generated. Most of the code, textures, models and assets were created by AI.

## Installation & Dependencies

1. Install the loader for your Minecraft version (Fabric / Forge / NeoForge);
2. Install **GeckoLib** (animation engine, required). The 1.21.1 build also uses the **Architectury API** .

**Optional integrations (recommended)**:
- **[Kaleidoscope Cookery](https://www.curseforge.com/minecraft/mc-mods/kaleidoscope-cookery)** (森罗物语厨房)
- **LegendarySurvivalOverhaul**: drinking also replenishes thirst when installed.
---

## Resource Pack Extension (Custom 3D Food Animations)
See the two example resource packs in this repository.
A food extension consists of a definition, model, animation, texture and sounds:

```text
assets/realisticdining/
├── definitions/
│   └── example_food.json
├── geo/
│   └── example_food.geo.json
├── animations/
│   └── example_food.animation.json
├── sounds/
└── textures/item/
    └── example_food.png
```

`definitions/example_food.json` example:

```json
{
  "item": "examplemod:example_food",
  "mode": "pickup",
  "invisible": ["exampelBone"],
  "sounds": {
    "sound_keyframe_name": "realisticdining:eat"
  }
}
```

- `item`: the item ID to override rendering for; items from any mod are supported.
- `mode`: holding mode. "static",shows the model immediately on pickup with procedural sway; "pickup" ,automatically plays a "pick up" animation on pickup and holds the final pose.
- `invisible`: bone names hidden during the idle (holding) stage. Hiding a parent bone also hides its children; they reappear during the eating animation.
- `sounds`: maps animation sound keyframe names to game sound IDs. `realisticdining:eat` maps to `realisticdining/sounds/eat.ogg`.

The model, animation and texture file names must match the definition file name. For example, if the definition file is `example_food.json`, use:

```text
geo/example_food.geo.json
animations/example_food.animation.json
textures/item/example_food.png
```

Name the arm bones `rightarm` / `leftarm` in the model.

The number of sub-animations in an animation file depends on `mode`:

| `mode` | Animations needed | Description |
|--------|-------------------|-------------|
| `static` | 1: `eat` | Show model on pickup, press U to play `eat` |
| `pickup` | 2: `pickup` + `eat` | `pickup` picks up to the holding pose, press U to play `eat` |

- Animation names: "eat"; in "pickup" mode use "pickup"and "eat".
- At the end of the animation, add an Animation Effects → Instructions → Script entry named finished in the timeline

Press **U** to play the eating animation.


---

## Keybinds

| Key | Function |
|-----|----------|
| `U` | Trigger drink/eat animation (holding a snack or drink) |
| `T` | Trigger rice-eating animation |
| `Y` | Toggle first-person arm animation rendering |

---

## Credits

- **Mod ID**: `realisticdining`
- **Version**: 2.3.0
- **Dependencies**: GeckoLib, Kaleidoscope Cookery (森罗物语厨房). 1.21.1 build: Architectury API + GeckoLib.

---

> This mod is AI-generated. Most of the code, textures, models and assets were created by AI.
