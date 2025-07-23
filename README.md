# ChickemMod 🐔

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21-green)](https://minecraft.net)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.102.0+-blueviolet)](https://fabricmc.net)
[![GitHub stars](https://img.shields.io/github/stars/Prarambha369/chikemMod?style=social)](https://github.com/Prarambha369/chikemMod/stargazers)

> Transform your Minecraft experience with magical, mountable chickens and enhanced breeding mechanics!

## 📦 Download

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/chikem-mod?color=00AF5C&label=Downloads&logo=modrinth&style=for-the-badge)](https://modrinth.com/mod/chikem-mod)
[![Modrinth Version](https://img.shields.io/modrinth/v/chikem-mod?color=00AF5C&label=Latest%20Version&logo=modrinth&style=for-the-badge)](https://modrinth.com/mod/chikem-mod/versions)

**[📥 Download from Modrinth](https://modrinth.com/mod/chikem-mod/versions)**

---

## 🚀 Current Features

### 🐓 Mountable Chickens
- **Ride chickens** with intuitive WASD controls
- **Smooth movement** and realistic animations
- **Right-click mounting/dismounting** system
- **Jump mechanics** while mounted (Space key)
- **Dismount with Left Shift**

### 🍯 Enhanced Feeding & Breeding System
- **GolChick Food** - Premium nutrition that works with vanilla breeding
- **Smart breeding** - GolChick Food can be used alongside vanilla seeds for breeding
- **Enhanced breeding chance** - 15% chance for special mountable chickens when using GolChick Food
- **Rare transformations** - Well-fed mountable chickens have small chance (0.75%) to become rare
- **Nutrition tracking** - Chickens remember feeding history with visual feedback
- **Growth acceleration** - Baby chickens grow faster when well-fed
- **10-second feeding cooldown** prevents spam feeding

### 🌟 Items & Mechanics
- **GoldieEgg** - Special throwable eggs (rare drop from special chickens)
- **GolChick Food** - Essential breeding item that works with all chickens
- **Breeding compatibility** - Regular vanilla chickens and special mountable chickens can both be bred with GolChick Food
- **Particle effects** - Beautiful visual feedback for well-fed chickens

### ⚡ Spawn Balance
- **Regular chickens spawn naturally** - 85% of all chicken spawns are regular vanilla chickens
- **Special chickens are rare** - Only 15% chance when breeding with GolChick Food
- **Rare chickens are very rare** - Only 0.75% overall chance for rare abilities

## 🎮 Controls

| Action | Control |
|--------|---------|
| Mount Chicken | Right-click with empty hand |
| Dismount | Left Shift |
| Movement (mounted) | WASD or Arrow Keys |
| Jump (mounted) | Space |
| Feed Chicken | Right-click with GolChick Food |

### 🐔 Breeding Guide
1. **Feed chickens** with GolChick Food to put them in breeding mode
2. **Regular vanilla chickens** can breed normally (100% chance for vanilla babies)
3. **Enhanced breeding** with GolChick Food gives 15% chance for mountable chicken babies
4. **Multiple feedings** increase chances of rare transformations over time
5. **Well-fed chickens** get regeneration and resistance effects

## 🛠️ Commands
- `/chickem` - Spawn special mountable chickens with rare abilities (creative/admin only)

## 🚀 Installation

### Prerequisites
- Minecraft 1.21
- Fabric Loader 0.14.0+
- Fabric API 0.102.0+

### Steps
1. Download the latest release from [Modrinth](https://modrinth.com/project/chikem-mod)
2. Place the `.jar` file in your Minecraft `mods` folder
3. Launch Minecraft with Fabric Loader
4. Enjoy enhanced chicken gameplay!

## 🛠️ Development

### Project Structure
```
src/main/java/mr/bashyal/chikemmod/
├── Chickenmod.java           # Main mod entrypoint  
├── ChickenmodCommands.java   # /chickem command implementation
├── entity/                   # MountableChickenEntity with rare abilities
├── item/                     # GolChick Food and GoldieEgg items
├── mixin/                    # Vanilla breeding integration
└── registry/                 # Game object registration
```

### Technical Details
- **Fabric Loader**: 0.16.14
- **Yarn Mappings**: 1.21+build.9
- **Mod Version**: 1.1.0-SNAPSHOT
- **Environment**: Client & Server compatible

### Building from Source
```bash
git clone https://github.com/Prarambha369/chikemMod.git
cd ChickenMod
./gradlew build
```

## 🗺️ Roadmap (Planned Features)

### 🌟 Upcoming Rare Chicken Breeds
- [ ] **Speed Chickens** - 25% faster movement
- [ ] **Slow Fall Chickens** - Reduced fall damage
- [ ] **Lucky Chickens** - 15% more drops
- [ ] **Dash Chickens** - Special dash ability with double-tap forward

### 🍯 Enhanced Feeding System (Planned)
- [ ] **Fertility Feed** - Accelerates breeding and growth
- [ ] **Premium Feed** - 30% chance for rare transformations
- [ ] **Mystic Feed** - 50% chance for special abilities

### 🔧 Technical Improvements
- [ ] Enhanced AI behaviors for rare chickens
- [ ] More particle effects and animations
- [ ] Performance optimizations
- [ ] Configuration options

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### Getting Started
1. Fork this repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Test thoroughly
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### Guidelines
- Follow existing code style and conventions
- Add tests for new features when possible
- Update documentation for significant changes
- Ensure compatibility with Minecraft 1.21 and Fabric API

## 🐛 Issues & Support

- **Bug Reports**: [GitHub Issues](https://github.com/Prarambha369/chikemMod/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/Prarambha369/chikemMod/discussions)
- **Questions**: Check existing issues or create a new one

## 📜 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

## 👥 Contributors

- **[MrBashyal](https://github.com/Prarambha369)** - Project Creator & Lead Developer
- **Community Contributors** - Thank you to everyone who has contributed!

## 🙏 Acknowledgments

- Fabric development team for the excellent modding framework
- Minecraft modding community for inspiration and support
- Beta testers and community feedback

---

**Made with ❤️ for the Minecraft community**

> 🐔 **"Because every chicken deserves to be rideable!"** - MrBashyal
