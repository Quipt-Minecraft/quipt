# Developer Guide

This guide shows how to extend QUIPT, use its APIs, and integrate with your server code. Examples below focus on the Paper environment, but core concepts are platform-agnostic.

Initialization on Paper
- Entry point: com.quiptmc.paper.Initializer (JavaPlugin)
- QUIPT is constructed via PaperIntegration and initialized on enable().

Events and Listeners
- The Paper module hooks into Bukkit and QUIPT’s own event utilities.
- Example: Registering a listener is handled in Initializer; you can add your own by following the same pattern.

Resource Pack Handler
- When ResourceConfig.repo_url is set, a ResourcePackHandler is mounted at /resources and can process updates.
- You can post a webhook to /resources/update/ from your VCS provider to trigger automatic pack updates.

Embedded Web Server (QuiptServer)
- QuiptServer wraps Jetty Server and is created with a ServerConfig:
  - protocol: HTTP or HTTPS
  - host: hostname/IP
  - port: port number
- Handlers can be registered on paths using server.handler().handle("route", yourHandler, "route/*").
- To enable: set WebConfig.enable = true and specify host/port.

Discord Bot Integration
- If DiscordConfig.enable_bot is true, QUIPT starts the bot asynchronously.
- On startup, it posts an embed to the configured server_status channel.
- You can extend the bot by adding JARs to plugins/quipt-paper/discord_bots. These are loaded via BotPluginLoader.

Registries and Materials
- QUIPT maps Bukkit Materials to a registry at enable-time:
  - MinecraftMaterial.init();
  - The registry can be accessed with MinecraftMaterial.registry().orElseThrow().
- Use registries to look up or register custom abstractions as needed.

Heartbeat and Scheduling
- HeartbeatUtils.heartbeat(integration).flutter(new Flutter { ... }) registers periodic tasks.
- Use TaskScheduler for async tasks, e.g., TaskScheduler.scheduleAsyncTask(runnable, delay, unit).

Integration Lifecycle
- com.quiptmc.core.QuiptIntegration provides:
  - enable() and shutdown()/destroy() coordination
  - logger() for structured logging
  - dataFolder() for persistent storage
  - api() for API communications via ApiManager

API Communication
- QuiptIntegration.ApiManager wraps API calls and update flows.
- Typical usage:
  - apiManager.initialize() during enable
  - apiManager.update() on schedule
  - api(url, jsonData) for custom endpoints; handle ApiResponse and results

Adding New HTTP Endpoints
- Create a handler compatible with Jetty’s handler model (see existing handlers like ResourcePackHandler).
- Register it using server.handler().handle("yourRoute", handler, "yourRoute/*").
- Ensure CORS and auth if exposing publicly; consider a reverse proxy.

Error Handling and Logging
- Use integration.logger().log(tag, message) for consistent logs.
- Check server console and logs when debugging.

Testing Tips
- Keep module boundaries clean. Core should stay platform-agnostic.
- Prefer dependency injection where possible for testability.
- Add small integration tests around handlers and configs when feasible.

Version Compatibility
- Check gradle.properties for minecraft_version and supported_minecraft_versions.
- For API-breaking changes, update documentation and consider semantic versioning in project_version.
