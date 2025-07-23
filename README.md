![CHICKEMMOD.png](src/main/resources/assets/CHICKEMMOD.png)
![License](https://img.shields.io/badge/license-MIT-purple)   ![Hackatime Badge](https://hackatime-badge.hackclub.com/U0894AG5K1C/ChickenMod/)
> Chikem Mod Yummy !!

## ChickemMod
ChickemMod is a modification designed to enhance Minecraft gameplay by introducing unique features and abilities related to chickens. From rare chicken breeds with special powers to customizable chicken behaviors, this mod adds an exciting layer of complexity and fun to the game.

## Features
- Rare chicken breeds with unique abilities (`SPEED`, `SLOW_FALL`, `LUCK`).
- Customizable chicken behaviors and spawn rates via JSON resources.
- Integration with core Minecraft gameplay mechanics.
- New items and foods related to chickens, including golden eggs and GolChick Food.
- Custom wandering trader trades for chicken-related items.
- Configurable breeding mechanics for chickens.
- Client features: custom entity renderers for enhanced visuals.

## Installation Steps
1. Download the mod file from [ChickemMod on Modrinth](https://modrinth.com/project/chikem-mod).
2. Place the downloaded `.jar` file into your Minecraft `mods` folder.
3. Launch the game with Fabric Loader.

## Contribution Guidelines
This project is open source, and contributions are welcome!
- Fork the repository on GitHub.
- Create a new branch for your changes.
- Submit a pull request with a detailed description of your changes.
- Report bugs or suggest features by opening an issue on the [GitHub repository](https://github.com/Prarambha369/chikemMod).

## Code Quality & Maintenance
- **Entrypoint:** Only `Chickenmod.java` is the main entrypoint. `ChickenMod.java` is deprecated and can be deleted.
- **Commands:** `/chickem` command spawns rare chickens. See `ChickenmodCommands.java` for usage and logic.
- **Mixins:** Used to allow custom breeding for chickens. Check `MixinChickenEntity.java` for details.
- **Networking:** Custom payload handles dash ability for rare chickens.

## Upgrade & Compatibility
- Uses Fabric API v2+ and is compatible with Minecraft 1.21 (see `fabric.mod.json`).
- If upgrading Minecraft/Fabric, check for breaking changes in Fabric API and update usages accordingly.
- Avoid reflection in commands for future-proofing.
- Standardize naming (use `Chickenmod` everywhere).

## Testing & CI
- Add automated tests for entity/item registration and command logic.
- Consider adding GitHub Actions or your preferred CI for build/test automation.

## Assets & Localization
- Assets and translations are in `src/main/resources/assets/chikemmod/lang/`.
- Add or update language files for more translations.

## Contributors
- Main author: Prarambha369 (MrBashyal)

## Technologies Used
- Fabric Mod Loader
- Java 17+
- Minecraft 1.21

## License
This project is licensed under the [MIT License](LICENSE.txt).

## Support
If you like this project, consider supporting its development by following the repository, starring it on GitHub, or making a donation to [NextEra-Development](https://github.com/NextEra-Development).

---
For detailed code documentation, see comments in source files.
