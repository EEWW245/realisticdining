# Changelog

## [1.0.3] - 2026-04-02

### Bug Fixes

- **Fixed Roast Chicken Block Drop Issue (NeoForge)**: Fixed an issue where breaking the Roast Chicken block with left-click did not drop the corresponding Roast Chicken item in the NeoForge version. Now breaking the block will properly drop the item.

- **Fixed Placeable Food Block Drop Issues**: Fixed an issue where Peppery Chicken Plate, Stir-Fried Pork Cabbage Plate, and Rice Bowl blocks would not drop their corresponding items when broken with left-click before being picked up with chopsticks. Now these blocks will properly drop their items when broken before interaction.

- **Fixed Tool Block Drop Issues**: Fixed an issue where breaking the Wok and Chopping Board blocks did not drop their corresponding items. Now both blocks will properly drop themselves when broken.

- **Fixed Dish Recipe Conflict**: Fixed a critical conflict between Peppery Chicken and Stir-Fried Pork Cabbage dishes where the 3D models would conflict when adding ingredients. The issue was caused by incorrect logic that allowed Stir-Fried Pork Cabbage to accept ingredients when the wok already had chicken for Peppery Chicken. Now each dish has its own independent cooking logic:
  - **Peppery Chicken**: Requires chopped chicken → chopped red pepper → chopped green onion (in order)
  - **Stir-Fried Pork Cabbage**: Requires chopped pork → chopped greens (in order)
  - The two dishes no longer interfere with each other's 3D model states

### Changes

- Updated version number to 1.0.3
- Improved code clarity and logic flow in WokBlockEntity
- Enhanced dish detection logic to prevent false positives

### Notes

This update focuses on fixing critical gameplay issues reported by the community. All players are recommended to update to this version for a better cooking experience.

---

**Realistic Dining** - A realistic cooking mod for Minecraft 1.21.1 (Fabric & NeoForge)
