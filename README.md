# ChickemMod 🐔

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21-green)](https://minecraft.net)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.102.0+-blueviolet)](https://fabricmc.net)
[![GitHub stars](https://img.shields.io/github/stars/Prarambha369/chikemMod?style=social&logoColor=red)](https://github.com/Prarambha369/chikemMod/stargazers)

> Transform your Minecraft experience with magical, mountable chickens and rare breeds with special abilities!

## 📝 Description
ChickemMod is a Minecraft mod that adds mountable chickens with unique abilities and a special feeding system.
Specifically, this mod adds mountable chickens with unique abilities and a special feeding system.
## 📦 Modrinth

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/chikem-mod?color=00AF5C&label=Downloads&logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod)
[![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/chikem-mod?color=00AF5C&label=Game%20Versions&logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod/versions)
[![Modrinth Version](https://img.shields.io/modrinth/v/chikem-mod?color=00AF5C&label=Latest%20Version&logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod/versions)
[![Modrinth followers](https://img.shields.io/modrinth/followers/chikem-mod?color=00AF5C&label=Followers&logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod/followers)
[![Modrinth Download Button](https://img.shields.io/badge/Download-Chikem%20Mod-darkgreen?logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod/versions)
[![View on Modrinth](https://img.shields.io/badge/View%20on%20Modrinth-00AF5C?logo=modrinth&style=for-the-badge&logoColor=red)](https://modrinth.com/mod/chikem-mod)


---

## 🚀 Features

### 🐓 Mountable Chickens
- Ride chickens with intuitive controls
- Smooth movement and animations
- Custom mounting/dismounting mechanics
- Basic rare chicken system with potential for special abilities

### 🍯 Enhanced Feeding System
- **GolChick Food** - Basic nutrition and health restoration with feeding cooldown system
- Chickens remember feeding history and become healthier with regular feeding
- 10-second feeding cooldown prevents spam
- Visual and audio feedback when feeding chickens

### 🌟 Nutrition System
- Chickens track how many times they've been fed
- Better nutrition increases chances of special events
- Enhanced healing (4.0 HP per feeding)
- Smart cooldown system prevents overfeeding

## 🚀 Installation

### Prerequisites
- Minecraft 1.21
- Fabric Loader 0.14.0+
- Fabric API 0.102.0+

### For Players
1. Download the latest release from [Modrinth](https://modrinth.com/project/chikem-mod) or [Releases](https://github.com/Prarambha369/chikemMod/releases)
2. Place the `.jar` file in your Minecraft `mods` folder
3. Launch Minecraft with Fabric Loader
4. Enjoy enhanced chicken gameplay!

### For Developers
```bash
# Clone the repository
git clone https://github.com/Prarambha369/chikemMod.git
cd ChickenMod

# Build the mod
./gradlew build
```
The built JAR will be in `build/libs/`

## 🎮 Controls & Gameplay

### Mounted Chicken Controls
- **Mount/Dismount**: Right-click with empty hand
- **Movement**: WASD or arrow keys
- **Jump**: Space (while mounted)
- **Dismount**: Left Shift
- **Dash (DASH ability)**: Double-tap forward (W key)
- **Special Ability**: V key (if applicable)

### Commands
- `/chickem` - Spawn rare chickens with special abilities
- Use with parameters to specify chicken types and abilities

### Feeding Strategy
1. **Use GolChick Food** to restore health and build nutrition over time
2. Feed chickens regularly to increase chances of special events
3. Wait for the 10-second cooldown between feedings to prevent waste

### Rare Chicken Creation
- **Current Method**: Feed any mountable chicken regularly with GolChick Food (chance varies based on nutrition)
- Better-fed chickens have higher chances of developing special abilities
- Look for visual and audio cues when feeding for optimal results

## 🛠️ Development

### Project Structure
```
src/main/java/mr/bashyal/chikemmod/
├── Chickenmod.java           # Main mod entrypoint
├── ChickenmodCommands.java   # Command implementations
├── entity/                   # Custom entities (Golden Eggs, Mountable Chickens)
├── item/                     # Custom items and food
├── mixin/                    # Vanilla game integration
├── network/                  # Multiplayer synchronization
├── registry/                 # Game object registration
└── effect/                   # Custom effects and abilities
```

### Technical Details
- **Fabric Loader**: 0.16.14
- **Yarn Mappings**: 1.21+build.9
- **Mod Version**: 1.1.0-SNAPSHOT
- **Environment**: Client & Server compatible

### Prerequisites for Development
- JDK 21
- IntelliJ IDEA (recommended)
- Basic Gradle knowledge

### Building
```bash
./gradlew build
```

## 🤝 Contributing

We welcome contributions from the community!

### Getting Started
1. Fork this repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes and test thoroughly
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

### Guidelines
- Follow existing code style and conventions
- Add tests for new features when possible
- Update documentation for significant changes
- Ensure compatibility with Minecraft 1.21 and Fabric API
- Main entrypoint: `Chickenmod.java` (ChickenMod.java is deprecated)

## 🔄 Roadmap

### Upcoming Features
- [ ] **Speed Chickens** - 25% faster movement
- [ ] **Slow Fall Chickens** - Reduced fall damage  
- [ ] **Lucky Chickens** - 15% more drops
- [ ] **Dash Chickens** - Special dash ability with double-tap forward
- [ ] Enhanced AI behaviors
- [ ] More custom items and recipes
- [ ] Performance optimizations
- [ ] Expanded trading systems
- [ ] **Fertility Feed** - Accelerates breeding and growth
- [ ] **Premium Feed** - 30% chance for rare transformations
- [ ] **Mystic Feed** - 50% chance for special abilities

### Compatibility
- **Minecraft**: 1.21+ (planning future version support)
- **Fabric**: Latest stable releases
- **Modpack**: Compatible with most Fabric modpacks

## 🐛 Support & Issues

- **Bug Reports**: [GitHub Issues](https://github.com/Prarambha369/chikemMod/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/Prarambha369/chikemMod/discussions)
- **Questions**: Check existing issues or create a new one

## 👥 Contributors

- **[MrBashyal](https://github.com/Prarambha369)** - Project Creator & Lead Developer
- **Community Contributors** - Thank you to everyone who has contributed!

## 🙏 Acknowledgments

- Fabric development team for the excellent modding framework
- Minecraft modding community for inspiration and support
- Beta testers and community feedback

## 📜 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

---

**Made with ❤️ for the Minecraft community by Prarambha aka MrBashyal** 

[![Twitter](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2FPrarambha369%2FchikemMod&logoColor=red)](https://twitter.com/intent/tweet?text=Check%20out%20ChickemMod%20for%20Minecraft!%20https%3A%2F%2Fgithub.com%2FPrarambha369%2FchikemMod)

If you enjoy this mod, consider giving it a ⭐ on GitHub!
