<div align="center">  
   
![Chikem Mod Banner](https://github.com/Prarambha369/chikemMod/blob/main/src/main/resources/assets/CHICKEMMOD.png?raw=true)   
[![GitHub Release](https://img.shields.io/github/v/release/Prarambha369/chikemMod?style=flat-square)](https://github.com/Prarambha369/chikemMod/releases)
[![GitHub Issues](https://img.shields.io/github/issues/Prarambha369/chikemMod?style=flat-square)](https://github.com/Prarambha369/chikemMod/issues)
[![GitHub License](https://img.shields.io/github/license/Prarambha369/chikemMod?style=flat-square)](LICENSE)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/chikem-mod?style=flat-square&logo=modrinth)](https://modrinth.com/mod/chikem-mod)
[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-green?style=flat-square)](https://www.minecraft.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.17.3%2B-blue?style=flat-square)](https://fabricmc.net/use/)
[![Fabric API](https://img.shields.io/badge/Dependency-Fabric%20API-orange.svg)](https://modrinth.com/mod/fabric-api)
[![Support](https://img.shields.io/badge/Support-Sponsor-ff69b4.svg?logo=githubsponsors)](https://github.com/sponsors/Prarambha369)

A comprehensive Fabric mod for Minecraft 1.21 that enhances gameplay with custom features and mechanics.
</div>

---

## Features

- **Custom Items**: Unique items with special properties and abilities
- **Enhanced Mechanics**: Improved gameplay systems and interactions
- **Performance Optimized**: Built with efficiency in mind for smooth gameplay
- **Fully Configurable**: Customize features to your preference
- **Multiplayer Compatible**: Works seamlessly on servers and clients

## Prerequisites

Before building or using this mod, ensure you have:

- **Java 21** or higher installed ([Download](https://adoptium.net/))
- **Minecraft 1.21** (Java Edition)
- **Fabric Loader 0.17.3** or higher ([Installation Guide](https://fabricmc.net/use/))
- **Fabric API** (installed separately, or resolved automatically by Modrinth)

## Installation

### For Players

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.11
2. Download the latest release from [Releases](https://github.com/Prarambha369/chikemMod/releases) or [Modrinth](https://modrinth.com/mod/chikem-mod) — Modrinth can resolve Fabric API for you
3. Place the `.jar` file in your `.minecraft/mods` folder
4. Launch Minecraft with the Fabric profile

### For Developers

1. Clone the repository:
   ```bash
   git clone https://github.com/Prarambha369/chikemMod.git
   cd chikemMod
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```
   (On Windows, use `gradlew.bat build`)

3. The compiled mod will be in `build/libs/chikem-mod-<version>.jar`

## Development Setup

1. Import the project into your IDE (IntelliJ IDEA or Eclipse)
2. Run `./gradlew genSources` to generate Minecraft sources
3. Use the Gradle tasks:
   - `runClient` - Launch Minecraft client with the mod
   - `runServer` - Start a test server with the mod
   - `build` - Compile the mod

## Project Structure

```
chikem-mod/
├── src/main/java/mr/bashyal/chikemmod/
│   ├── ChikemMod.java          # Main mod class
│   ├── items/                   # Custom items
│   ├── blocks/                  # Custom blocks
│   └── mixin/                   # Mixin classes
├── src/main/resources/
│   ├── fabric.mod.json          # Mod metadata
│   ├── chikem-mod.mixins.json   # Mixin configuration
│   └── assets/chikem-mod/       # Textures, models, sounds
├── build.gradle                 # Build configuration
└── gradle.properties            # Project properties
```

## Building

### Debug Build
```bash
./gradlew build
```

### Release Build
```bash
./gradlew clean remapJar
```

The release-ready remapped JAR will be located in `build/libs/` as `chikem-mod-<version>.jar`.

## Automated GitHub Releases

The release workflow in `.github/workflows/release.yml` is tag-driven and publishes one clean mod JAR.

1. Update `mod_version` in `gradle.properties`.
2. Commit and push your changes.
3. Create and push a matching tag (`v<mod_version>`), for example:

```bash
git tag v1.4.1
git push origin v1.4.1
```

The workflow will:
- run `remapJar`
- publish `chikem-mod-<version>.jar` as the release asset
- generate concise release notes from project metadata

## Configuration

Configuration files are generated in `.minecraft/config/chikem-mod/` on first launch.

## Probability & Loot Rates

### Special Chickem breeding chance
When **both parent chickens are fed GolChick Food** before breeding:

- **Peaceful**: 30%
- **Easy**: 24%
- **Normal**: 18%
- **Hard**: 14%
- **Hardcore**: 12%

### Natural treasure sources
- **GolChick Food**
  - Structure treasure chests: **35%**
  - Fishing treasure: **8%**
- **Goldie Egg**
  - Structure treasure chests: **12%**
  - Fishing treasure: **3%**

Included structure chest pools: simple dungeon, abandoned mineshaft, desert pyramid, jungle temple, stronghold corridor, ancient city, end city treasure, and bastion treasure.

## Changelog

### Version 1.4.1 (Current)
- Updated to Minecraft 1.21.11
- Updated Fabric Loader requirement to 0.17.3+
- Updated Yarn mappings to 1.21.11+build.1
- Improved build configuration
- Enhanced project structure
- Added automated CI/CD pipeline
- Standardized naming conventions
- Performance improvements

### Version 1.0.0
- Initial release
- Basic mod functionality

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/Prarambha369/chikemMod/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Prarambha369/chikemMod/discussions)

## Credits

- **Author**: [Prarambha369](https://github.com/Prarambha369)
- **Fabric Team**: For the amazing modding framework
- **Community**: For testing and feedback

## Links

- [GitHub Repository](https://github.com/Prarambha369/chikemMod)
- [Modrinth Page](https://modrinth.com/mod/chikem-mod)
- [Fabric Documentation](https://fabricmc.net/wiki/)

---

Made with ❤️ for the Minecraft modding community
