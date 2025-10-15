# Modules and Responsibilities

This project is a multi-module Gradle build. Below is a concise guide to each module, its purpose, and notable entry points.

common
- Purpose: Cross-platform utilities shared between server implementations.
- Contents: Shared models, helpers, and utilities that do not depend on Paper or Fabric specifics.

core
- Purpose: Platform-agnostic runtime and infrastructure.
- Highlights:
  - QuiptIntegration: Base lifecycle, logging, API manager, shutdown coordination.
  - QuiptServer (Jetty): Embedded web server with simple handler routing and address helpers.
  - ConfigManager and config files (ApiConfig, WebConfig, ResourceConfig, JenkinsConfig, MessagesConfig, DiscordConfig).
  - Heartbeat/Flutter: Lightweight scheduling utility for periodic tasks.
  - HTTP utilities, registries, and logger abstractions.

paper
- Purpose: Paper/Bukkit server integration and user-facing features.
- Entry: com.quiptmc.paper.Initializer (JavaPlugin).
- Features:
  - Registers MinecraftMaterial registry for Bukkit materials.
  - Registers listeners (PlayerListener, EventListener, SessionListener; plus QuiptPlayerListener from minecraft package).
  - Starts QuiptServer and mounts handlers like /resources when configured.
  - Launches Discord bot if enabled in DiscordConfig.
  - Commands via CommandManager.

fabric
- Purpose: Fabric mod integration (structure and shared code/hooks for Fabric runtime).
- Note: Some Paper-specific features (Bukkit events, Paper commands) are not applicable. Ensure Fabric loader and mappings match your game version.

bot
- Purpose: Discord bot runtime and APIs (JDA-based) used by QUIPT or standalone.
- Features:
  - Bot.start(token/config) bootstrap.
  - QuiptGuild, QuiptTextChannel abstractions and helpers.
  - BotPlugin loader to dynamically load bot extensions from a folder.

src (root)
- Purpose: May contain root-level utilities or shared resources for packaging and tests.

Build and Versions
- Java: 21 (see gradle.properties).
- Dependencies include: JDA (Discord), Jetty (HTTP server), Jackson/JSON, HikariCP, etc. See gradle.properties for versions.
- Minecraft target: See minecraft_version and supported_minecraft_versions in gradle.properties.
