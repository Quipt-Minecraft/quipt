# QUIPT Documentation: Overview

QUIPT is a multi-module toolkit for Minecraft servers and developers. It provides runtime services, a lightweight web server, configuration management, a Discord bot integration, developer-friendly APIs, and ready-to-use features for Paper and Fabric environments.

This document gives a high-level view of the architecture and how the pieces fit together.

- Modules
  - core: Platform-agnostic runtime, configuration, logging, Jetty-based QuiptServer, heartbeat/task scheduling, HTTP utilities, registries.
  - common: Cross-platform utilities shared by Paper and Fabric layers.
  - paper: Paper plugin that initializes QUIPT on Bukkit/Paper servers, registers listeners/commands, exposes developer APIs, resource pack updater, optional Jetty endpoints.
  - fabric: Fabric mod layer (structure present; feature parity may vary by version).
  - bot: Discord bot runtime and API bindings (JDA-based) with plugin loading and basic guild/channel helpers.

- Key Features
  - Resource pack management with webhook-triggered updates.
  - Teleportation and session utilities for servers.
  - Embedded HTTP server (Jetty) for callbacks, resource hosting, and future health endpoints.
  - Discord bot integration: status reporting and plugin loading.
  - Developer APIs: events, registries, ephemeral advancements/toasts, placeholders, message utils.

- Typical Flow on Paper
  1) Server starts the Paper plugin (Initializer).
  2) QUIPT core initializes: configs are registered and saved if missing.
  3) QuiptServer (Jetty) starts if enabled, mounts handlers like resources/ and callback endpoints.
  4) Optional Discord bot launches and posts a startup embed to a configured channel.
  5) Event listeners, placeholders, message systems, and sessions are activated.

- Notable Classes
  - com.quiptmc.core.QuiptIntegration: Base integration lifecycle, logging, API manager, graceful shutdown.
  - com.quiptmc.core.server.QuiptServer: Embedded Jetty server with simple handler routing and address config.
  - com.quiptmc.paper.Initializer: Paper plugin entry. Wires configs, listeners, QuiptServer, ResourcePackHandler, and Discord bot.
  - com.quiptmc.minecraft.utils.*: In-game utilities for messages, placeholders, sessions.
  - com.quiptmc.discord.*: Discord bot bootstrap, API models, plugin loader.

See also:
- MODULES.md for per-module responsibilities
- INSTALLATION.md for setup instructions
- CONFIGURATION.md for full config reference
- DEVELOPER.md for API usage examples