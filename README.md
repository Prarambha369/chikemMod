# ChickemMod

![CHICKEMMOD.png](src/main/resources/assets/CHICKEMMOD.png)
![License](https://img.shields.io/badge/license-MIT-purple)   ![Hackatime Badge](https://hackatime-badge.hackclub.com/U0894AG5K1C/ChickenMod/)

> Chikem Mod Yummy !!

## About ChickemMod

ChickemMod is a modification designed to enhance Minecraft gameplay by introducing unique features and abilities related to chickens. From rare chicken breeds with special powers to customizable chicken behaviors, this mod adds an exciting layer of complexity and fun to the game.

## Features

- **Rare chicken breeds** with unique abilities (`SPEED`, `DASH`, `SLOW_FALL`, `LUCK`)
- **Customizable chicken behaviors** and spawn rates via JSON resources
- **Integration** with core Minecraft gameplay mechanics
- **New items and foods** related to chickens, including golden eggs and GolChick Food
- **Custom wandering trader trades** for chicken-related items
- **Configurable breeding mechanics** for chickens
- **Client features**: custom entity renderers, key binding for dash ability (default: `J`)

## Download

**📥 Get ChickemMod:**
- [Download from Modrinth](https://modrinth.com/mod/chikem-mod) (Recommended)

## Dependencies

**Required:**
- [Fabric Loader](https://fabricmc.net/use/installer/) (latest version)
- [Fabric API](https://modrinth.com/mod/fabric-api) (compatible with Minecraft 1.21)

**Note:** The mod will not start without Fabric API installed. Make sure to download it alongside the mod.

## Installation Steps

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for Minecraft 1.21
3. Download ChickemMod from [Modrinth](https://modrinth.com/project/chikem-mod)
4. Place both `.jar` files (Fabric API and ChickemMod) into your Minecraft `mods` folder
5. Launch the game with the Fabric profile

## How to Use

### Getting Started
- Once installed, chickens will naturally spawn with special abilities
- Look for chickens with different visual indicators or behaviors
- Use the `J` key (default) to activate dash ability when near special chickens

### Special Chicken Types
- **Speed Chickens**: Move faster than normal chickens
- **Dash Chickens**: Can perform quick dash movements (activate with `J` key)
- **Slow Fall Chickens**: Fall slowly and take no fall damage
- **Luck Chickens**: Provide luck effects to nearby players

### Special Items
- **Golden Eggs**: Rare drops from special chickens with enhanced properties
- **GolChick Food**: Special chicken feed that can influence breeding outcomes
- **Trader Items**: Visit wandering traders for exclusive chicken-related goods

### Breeding
- Use GolChick Food to breed chickens with higher chances of special abilities
- Experiment with different combinations to discover new chicken types
- Check JSON configuration files to customize spawn rates and behaviors

## Configuration

The mod supports JSON-based configuration for:
- Chicken spawn rates
- Ability probabilities
- Custom behaviors
- Breeding mechanics

Configuration files can be found in your `.minecraft/config` folder after first launch.

## Technologies Used

- Fabric Mod Loader
- Java 17+
- Minecraft 1.21
- Fabric API

## Installation Requirements

- **Minecraft Version**: 1.21
- **Mod Loader**: Fabric Loader (latest)
- **Java Version**: 17 or higher
- **Required Dependencies**: Fabric API

## Development

### Building from Source
```bash
git clone https://github.com/Prarambha369/chikemMod.git
cd chikemMod
./gradlew build
```

### Project Structure
```
src/
├── main/
│   ├── java/          # Java source code
│   └── resources/     # Assets and data files
└── test/              # Unit tests
```

## Contribution Guidelines

This project is open source, and contributions are welcome!

- Fork the repository on GitHub
- Create a new branch for your changes
- Submit a pull request with a detailed description of your changes
- Report bugs or suggest features by opening an issue on the [GitHub repository](https://github.com/Prarambha369/chikemMod)

## License

This project is licensed under the [MIT License](LICENSE.txt).

## Support

If you like this project, consider supporting its development by:
- Following the repository
- Starring it on GitHub
- Making a donation to [NextEra-Development](https://github.com/NextEra-Development)
- Reporting bugs and suggesting features

## Troubleshooting

**Common Issues:**
- **Game won't start**: Make sure Fabric API is installed alongside the mod
- **No special chickens spawning**: Check configuration files and ensure proper installation
- **Dash ability not working**: Verify key binding in controls settings (default: `J`)

For more help, please open an issue on the GitHub repository.
