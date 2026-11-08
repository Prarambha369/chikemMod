<div align="center">

![Chikem Mod Banner](https://github.com/Prarambha369/chikemMod/blob/main/src/main/resources/assets/CHICKEMMOD.png?raw=true)
[![Minecraft Version](https://img.shields.io/badge/Minecraft-26.3-green?style=flat-square)](https://www.minecraft.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.20.1%2B-blue?style=flat-square)](https://fabricmc.net/use/)
[![Java](https://img.shields.io/badge/Java-25%2B-orange?style=flat-square)](https://adoptium.net/)

A comprehensive Fabric mod for Minecraft 26.3 that enhances gameplay with custom features and mechanics.
</div>

---

## Features

- **Custom Items**: GolChick Food and Goldie Egg with special properties and abilities
- **Special Chickems**: Four rideable chicken types (Swiftwing, Dashclaw, Featherfall, Lucky Beak)
- **Enhanced Mechanics**: Improved gameplay systems and interactions
- **Multiplayer Compatible**: Works seamlessly on servers and clients

## Prerequisites

Before building or using this mod, ensure you have:

- **Java 25** or higher installed ([Download](https://adoptium.net/))
- **Minecraft 26.3** (Java Edition)
- **Fabric Loader 0.20.1** or higher ([Installation Guide](https://fabricmc.net/use/))
- **Fabric API** (installed separately, or resolved automatically by Modrinth)

## Installation

### For Players

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.3
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

## Special Chickems

Four lore-rich Special Chickems you can breed and ride:

### Swiftwing — The Storm-Hatched
Hatched from an egg that rolled into a lightning scar during a mesa storm, Swiftwing learned to read the wind before it could walk. It does not run — it chases weather, ferrying swift seeds to barren soil and scouting clear skies for the flock. Ability: fastest mount in the flock. Drawback: fragile — one less heart than a normal chicken.

### Dashclaw — The Ambush Striker
Born in a fox-raided coop, Dashclaw survived by striking first and vanishing. Its golden leg feathers are scar tissue from the night it escaped. It asks no fight — only distance. Ability: recharging dash burst. Drawback: brief slowdown after each dash.

### Featherfall — The Quiet Descent
An ancient city's last chick, found sleeping in a clean chest guarded by a sculk warden who never woke her. She learned that the safest way down is to be too quiet to notice. Ability: fall-damage immune, glides while falling. Drawback: slow on ground and in air.

### Lucky Beak — The Gilded Trickster
The only chick ever hatched inside a treasure chest, Lucky Beak carries a dull gold nugget in her beak that she swears is a soul-gem. It isn't. But fortune seems to loiter near her anyway. Ability: golden aura blessing nearby players with Luck. Drawback: blesses only travelers within sight.

Use `/chickem` (permission level 2) to spawn a random Special Chickem.

## Project Structure

```
chikem-mod/
├── src/main/java/mr/bashyal/chikemmod/
│   ├── ChikemMod.java
│   ├── ChikemmodCommands.java
│   ├── entity/
│   │   ├── BreedingRates.java
│   │   ├── ChickenIdentity.java
│   │   ├── GolChickBreedingTracker.java
│   │   └── MountableChickenEntity.java
│   ├── item/
│   │   ├── GolChickFoodItem.java
│   │   └── GoldieEggItem.java
│   ├── mixin/
│   │   └── ChickenBreedingMixin.java
│   ├── network/
│   │   ├── DashRequestC2SPayload.java
│   │   └── ModNetworking.java
│   ├── registry/
│   │   ├── ModEntities.java
│   │   └── ModItems.java
│   └── world/
│       └── ModLootInjector.java
├── src/main/resources/
│   ├── fabric.mod.json
│   ├── chikem-mod.mixins.json
│   └── assets/chikem-mod/       # lang, models, textures
├── src/client/java/mr/bashyal/chikemmod/client/
│   └── ChickenmodClient.java
├── src/test/java/mr/bashyal/chikemmod/
│   └── ChickenmodBasicTest.java
├── build.gradle
├── gradle.properties
└── settings.gradle
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
  - Structure treasure chests: **35%** (pool chance)
  - Fishing treasure: **8%** per treasure roll (vanilla treasure pool weight is 5%, so the effective per-catch rate is lower — honest rate, not an advertised flat rate)
- **Goldie Egg**
  - Structure treasure chests: **12%** (pool chance)
  - Fishing treasure: **3%** per treasure roll (same 5% vanilla-weight caveat)

Included structure chest pools: simple dungeon, abandoned mineshaft, desert pyramid, jungle temple, stronghold corridor, ancient city, end city treasure, and bastion treasure. Structure counts: GolChick Food 1–2, Goldie Egg 1.

Note: Goldie Egg uses vanilla egg behavior — it hatches a plain chick, not a Special Chickem.

## Changelog

### Version 1.5.0 (Current)
- Updated to Minecraft 26.3
- Updated Fabric Loader requirement to 0.20.1+
- Migrated to official Mojang mappings (MojMap); Yarn removed
- Java 25 / release 25, loom 1.19.4, Gradle 9.8.2
- Special Chickems lore redesign (ChickenIdentity, custom naming, backstories)
- Audit fixes: rider control, tracked type sync, command permissions, honest loot docs

### Version 1.4.1
- Updated to Minecraft 1.21.11
- Updated Fabric Loader requirement to 0.17.3+

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

## Links

- [GitHub Repository](https://github.com/Prarambha369/chikemMod)
- [Modrinth Page](https://modrinth.com/mod/chikem-mod)
- [Fabric Documentation](https://fabricmc.net/wiki/)

---

Made with ❤️ for the Minecraft modding community
