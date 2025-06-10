# ChickenMod

![CHICKEMMOD.png](src/main/resources/assets/CHICKEMMOD.png)
![License](https://img.shields.io/badge/license-MIT-blue)   ![Hackatime Badge](https://hackatime-badge.hackclub.com/U0894AG5K1C/ChickenMod)
> Chikem Mod Yummy !!

## Overview
ChickenMod enhances Minecraft gameplay by introducing rare, mountable chickens with special abilities, new items, and custom mechanics. The mod is built for Fabric and is open source.

## Current Features
- **Rare Mountable Chickens:**
  - Rare chickens spawn naturally (1 in 612 chance) and can be ridden by players.
  - Each rare chicken has a special ability: `SPEED`, `DASH`, `SLOW_FALL`, or `LUCK`.
  - Rare chickens lay golden eggs; normal chickens lay regular eggs.
  - Chicken names and abilities are loaded from JSON resources.
- **Special Abilities:**
  - `SPEED`: Increased movement speed.
  - `DASH`: Player can trigger a dash burst (default key: `J`).
  - `SLOW_FALL`: Grants slow falling effect to the rider.
  - `LUCK`: Grants luck effect to the rider.
- **Commands:**
  - `/chickem` — Spawns a rare chicken.
  - `/chickem <ability>` — Spawns a rare chicken with the specified ability.
- **Items & Trades:**
  - Golden eggs from rare chickens.
  - Custom food item (GolChick Food) available from wandering traders.
- **Client Features:**
  - Custom entity renderers for mountable chickens and golden eggs.
  - Key binding for dash ability (default: `J`).

## Usage Examples
- **Spawn a rare chicken:** `/chickem`
- **Spawn a rare chicken with dash ability:** `/chickem dash`
- **Trigger dash:** Press `J` while riding a rare chicken with the dash ability.

## Recent Changes
- Bug fixes and code cleanup for client and server code.
- Improved documentation and code comments.
- Enhanced command system to support ability selection.
- Removed deprecated or problematic mixins for better compatibility.

## Future Plans
- Add more special abilities and rare chicken variants.
- Expand commands and configuration options.
- Add more items, foods, and effects.
- Enhance client-side features (sounds, animations, UI).
- Improve integration with other mods and Minecraft mechanics.

## Installation
1. Download the mod file from [ChickenMod on Modrinth](https://modrinth.com/project/chikem-mod).
2. Place the downloaded `.jar` file into your Minecraft `mods` folder.
3. Launch the game with Fabric Loader.

## Contribution Guidelines
- Fork the repository on GitHub.
- Create a new branch for your changes.
- Submit a pull request with a detailed description.
- Report bugs or suggest features by opening an issue.

## Technologies Used
- Fabric Mod Loader
- Java

## License
This project is licensed under the [MIT License](LICENSE).

## Support
If you like this project, consider supporting its development by following, starring, or donating to [NextEra-Development](https://github.com/NextEra-Development).
