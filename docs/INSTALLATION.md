# Installation and Setup

This guide explains how to install and run QUIPT across supported platforms and how to build from source.

Prerequisites
- Java 21 (match java_version in gradle.properties)
- For building: Git and Gradle (or use the Gradle wrapper)
- For Paper servers: a compatible Paper build (see supported_minecraft_versions)
- For Fabric: a matching Fabric Loader and Yarn mappings (see gradle.properties)

Option A: Install Prebuilt Release (Recommended)
1) Download the latest release:
   - Modrinth: https://www.modrinth.com/plugin/QUIPT
   - GitHub Releases: https://github.com/Quipt-Minecraft/quipt/releases
2) Drop the artifact into your server/mod loader:
   - Paper: place the plugin JAR into server_folder/plugins/
   - Fabric: place the mod JAR into server_folder/mods/
3) Start the server once to generate configuration files.
4) Configure QUIPT (see CONFIGURATION.md) and restart the server.

Option B: Build From Source
1) Clone the repository:
   - git clone https://github.com/Quipt-Minecraft/quipt.git
2) Build with Gradle:
   - On Windows PowerShell: .\gradlew.bat build
   - On other platforms: ./gradlew build
3) The built artifacts will be under each module's build/libs directory.
4) Install as in Option A.

Platform Notes
- Paper (Bukkit): The paper module provides the plugin entry (com.quiptmc.paper.Initializer). Most features are available here including the embedded web server and Discord bot integration.
- Fabric: Project layout is present; ensure the module’s published artifact targets your Fabric version. Some Paper-specific features (like Bukkit events) will not apply.
- Discord Bot: The bot module can be automatically launched by the Paper plugin when enabled in DiscordConfig. You can also extend functionality with bot plug-ins by placing JARs in plugins/quipt-paper/discord_bots.

Resource Pack Auto-Updates
- Configure your resource pack repository URL and server IP in resources.json (ResourceConfig).
- Optionally set up a webhook to http://<server-host>:<port>/resources/update/ to trigger updates on push events.

Troubleshooting
- Ensure the configured host/port for the embedded web server is reachable and not blocked by a firewall.
- Verify Java 21 is used (java -version) and matches the server runtime.
- If the bot fails to start, double-check the token and channel IDs in DiscordConfig.
- Consult logs/quipt/*.log or the server console for detailed error messages.