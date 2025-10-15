# QUIPT [![Quipt](https://github.com/Quipt-Minecraft/quipt/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/Quipt-Minecraft/quipt/actions/workflows/gradle-publish.yml)
[![available on github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg)](https://github.com/QuickScythe/QUIPT)
[![available on Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://www.modrinth.com/plugin/QUIPT)
[![discord](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.gg/EhfMJmjTXh)
[![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg)](https://papermc.io/downloads/paper)
![bukkit](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/bukkit_vector.svg)
![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)

QUIPT is a multi-module toolkit for Minecraft servers and developers. It provides:
- Paper and Fabric integration layers
- An embedded HTTP server (Jetty) for callbacks/resources
- Resource pack auto-updates via webhooks
- A Discord bot integration with plugin loading
- Developer APIs for events, registries, messaging, placeholders, and ephemeral advancements

Quick Links
- Overview: docs/OVERVIEW.md
- Installation: docs/INSTALLATION.md
- Configuration Reference: docs/CONFIGURATION.md
- Modules: docs/MODULES.md
- Developer Guide: docs/DEVELOPER.md
- Contributing: docs/CONTRIBUTING.md

Getting Started (Paper)
1) Download a release from Modrinth or GitHub and place the JAR in plugins/.
2) Start your server once to generate default configs.
3) Edit configs under plugins/quipt-paper/ (see docs/CONFIGURATION.md).
4) Restart the server. QUIPT will start the embedded web server and Discord bot if enabled.

Server Owner Features
- Resource Pack Management: Configure resources.json with your pack repo and server IP. Trigger updates via /resourcepack update or a webhook to http://<host>:<port>/resources/update/.
- Teleportation Management: Simple P2P teleports and configurable warps/homes.

Developer Features
- Events and Listeners: Ready-to-use listeners and an event system to hook into.
- Ephemeral Advancements: Send toast-like updates to players using Bukkit advancements under the hood.
- Embedded Web Server: Mount your own handlers at custom routes.
- Discord Integration: Post server status and extend via bot plugins.

Building From Source
- Requirements: Java 21, Git
- Windows PowerShell: .\gradlew.bat build
- Other platforms: ./gradlew build
- Artifacts are available under each module's build/libs directory.

Support and Community
- Discord: https://discord.gg/EhfMJmjTXh
- Issues: https://github.com/Quipt-Minecraft/quipt/issues

Versioning
- See gradle.properties for project_version and supported_minecraft_versions.

License
- Copyright © QuickScythe.
- See repository license file for details.
