# ChickenMod 🐔

![CHICKEMMOD.png](src/main/resources/assets/CHICKEMMOD.png)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-green.svg)](https://minecraft.net)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.102.0+-blue.svg)](https://fabricmc.net)
![Hackatime Badge](https://hackatime-badge.hackclub.com/U0894AG5K1C/ChickenMod/)

> Enhance your Minecraft experience with magical chickens and new gameplay mechanics!

## 📖 About

ChickenMod is a comprehensive Minecraft modification built for Fabric 1.21 that revolutionizes chicken gameplay. From mountable chickens to rare breeds with special abilities, this mod adds depth and excitement to the humble chicken while maintaining vanilla-friendly balance.

## ✨ Features

### 🐓 Enhanced Chickens
- **Mountable Chickens**: Ride chickens like horses with custom mounting mechanics
- **Rare Chicken Breeds**: Special chickens with unique abilities:
  - `SPEED` - Enhanced movement speed
  - `SLOW_FALL` - Gentle descent ability
  - `LUCK` - Increased drop rates and fortune
- **Custom Chicken Behaviors**: Configurable AI and spawn mechanics

### 🥚 New Items & Food
- **Golden Eggs**: Special projectile entities with unique properties
- **GolChick Food**: Premium chicken feed for breeding and taming
- **Custom Item Mechanics**: Enhanced interaction systems

### 🛒 Trading & Economy
- **Wandering Trader Integration**: New chicken-related trades
- **Configurable Trade Systems**: Customizable merchant offerings

### ⚙️ Technical Features
- **Custom Entity Renderers**: Enhanced visual effects and animations
- **Mixin Integration**: Seamless vanilla game integration
- **Network Synchronization**: Multiplayer-compatible features
- **JSON Configuration**: Data-driven mod behavior

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

## 🎮 Usage

### Commands
- `/chickem` - Spawn rare chickens with special abilities
- Use with parameters to specify chicken types and abilities

### Gameplay
- **Mounting**: Right-click on compatible chickens to mount them
- **Breeding**: Use GolChick Food for enhanced breeding mechanics
- **Trading**: Find wandering traders for exclusive chicken-related items

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

### Code Quality Guidelines
- Main entrypoint: `Chickenmod.java` (ChickenMod.java is deprecated)
- Mixins handle vanilla breeding mechanics integration
- Custom networking for multiplayer dash abilities
- JSON-based configuration for data-driven features

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

### Getting Started
1. Fork this repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Test thoroughly
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### Development Setup
```bash
git clone https://github.com/Prarambha369/chikemMod.git
cd ChickenMod
./gradlew build
```

### Guidelines
- Follow existing code style and conventions
- Add tests for new features when possible
- Update documentation for significant changes
- Ensure compatibility with Minecraft 1.21 and Fabric API

## 🐛 Issues & Support

- **Bug Reports**: [GitHub Issues](https://github.com/Prarambha369/chikemMod/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/Prarambha369/chikemMod/discussions)
- **Questions**: Check existing issues or create a new one

## 🔄 Roadmap

### Upcoming Features
- [ ] Additional rare chicken breeds
- [ ] Enhanced AI behaviors
- [ ] More custom items and recipes
- [ ] Performance optimizations
- [ ] Expanded trading systems

### Compatibility
- **Minecraft**: 1.21+ (planning future version support)
- **Fabric**: Latest stable releases
- **Modpack**: Compatible with most Fabric modpacks

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

*For more updates and content, follow the project on GitHub!*
