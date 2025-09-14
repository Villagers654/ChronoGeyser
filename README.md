# ChronoGeyser

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

**ChronoGeyser** is a **fork of [Geyser](https://geysermc.org/)** - the popular proxy that enables cross-play between Minecraft: Bedrock Edition and Java Edition.
While Geyser focuses on the latest versions of Bedrock, ChronoGeyser extends support to **legacy Bedrock versions**, giving players on older clients the ability to connect to modern Java servers.

---

## What is ChronoGeyser?

ChronoGeyser is a proxy that lets **Minecraft: Bedrock Edition** players join **Minecraft: Java Edition** servers.
Unlike upstream Geyser, which only supports the most recent Bedrock releases, ChronoGeyser **bridges the gap across time** by adding backwards compatibility for older Bedrock clients.

* 🔹 Current support: **Bedrock 1.21.50 – 1.21.100**
* 🔮 Roadmap: Extend backwards to **1.17.10+**

This makes ChronoGeyser especially valuable for players and server communities who want to keep older Bedrock clients playable without being forced to use closed source Nukkit projects.

Special thanks go to:

* [GeyserMC](https://geysermc.org/) for the original project
* [DragonProxy](https://github.com/DragonetMC/DragonProxy) for pioneering early protocol translation work

---

## Supported Versions

* **Bedrock Edition**:

  * ✅ Current: **1.21.50 – 1.21.100**
  * 🚧 Planned: **1.17.10 and above**

* **Java Edition**: Matches the versions supported by Geyser, [ViaVersion](https://github.com/ViaVersion/ViaVersion) can be used to extend Java support.

---

## Setting Up

ChronoGeyser setup is identical to Geyser’s, it should be a drop-in replacement. You can see their setup guide [here](https://geysermc.org/wiki/geyser/setup/)

---

## Links

* Docs: *Coming soon*
* Download: [Releases](https://github.com/Villagers654/ChronoGeyser/releases)
* Discord: *Coming soon*
* Donate: *Coming soon*
* Test Server: *Coming soon*

---

## Goals and Roadmap

* Expand stable support for **Bedrock 1.17.10 → latest**
* Keep upstream Geyser features and compatibility
* Smooth out movement, combat, and entity sync across different Bedrock generations
* Preserve playability for older Bedrock clients on modern Java servers

---

## Known Limitations

Some features cannot be implemented due to fundamental differences between versions of the game, such as dried ghasts in version 1.21.70 and older.

---

## Compiling

1. Clone the repo:

   ```bash
   git clone https://github.com/Villagers654/ChronoGeyser.git
   ```
2. Initialize submodules:

   ```bash
   git submodule update --init --recursive
   ```
3. Build with Gradle:

   ```bash
   ./gradlew build
   ```
4. Artifacts will appear in `bootstrap/build`.

---

## Contributing

ChronoGeyser is a **community project** - contributions are welcome!
Whether you’re interested in coding, testing legacy versions, or improving documentation, we’d love your help.

---

## Libraries Used

ChronoGeyser inherits its libraries from upstream:

* [Adventure Text Library](https://github.com/KyoriPowered/adventure)
* [CloudburstMC Bedrock Protocol Library](https://github.com/CloudburstMC/Protocol)
* [MCProtocolLib](https://github.com/GeyserMC/MCProtocolLib)
* [TerminalConsoleAppender](https://github.com/Minecrell/TerminalConsoleAppender)
* [SLF4J](https://github.com/qos-ch/slf4j)
