# Configuration Reference

QUIPT writes configuration files on first run. This document describes the major config files and key fields referenced in the codebase.

Locations
- Paper: plugins/quipt-paper/ (and subfolders like discord_bots)
- Fabric: config/quipt/ (typical Fabric config path; verify in your deployment)

Core Configs
- ApiConfig (api.json)
  - purpose: Stores metadata and API configuration used by QuiptIntegration.ApiManager.
  - example fields:
    - enable: boolean – enable periodic API syncs/updates.
    - endpoint: string – base URL of your integration endpoint.
    - apiKey: string – authorization token if required.

- WebConfig (web.json)
  - purpose: Controls the embedded Jetty server (QuiptServer).
  - fields:
    - enable: boolean – start the embedded web server when true.
    - host: string – interface hostname or IP (e.g., 0.0.0.0 to listen on all).
    - port: number – TCP port to bind (e.g., 8080).

- ResourceConfig (resources.json)
  - purpose: Enables automatic resource pack updates and hosting.
  - fields:
    - repo_url: string – public Git URL of your resource pack repository.
    - serverIp: string – external address for pack delivery (used by clients).
  - behavior:
    - When repo_url is set, ResourcePackHandler is mounted at /resources and can receive an update webhook on push.

- DiscordConfig (discord.json)
  - purpose: Configures the Discord bot integration.
  - fields:
    - enable_bot: boolean – start the bot asynchronously on server enable.
    - token: string – bot token from your Discord application.
    - channels.server_status: string – channel ID or name to post server status embeds to.
  - notes:
    - On startup, the bot posts a green "Server has started" embed to the configured channel.

- JenkinsConfig (jenkins.json)
  - purpose: Reserved for CI/CD interactions or artifact checks (if you integrate build notifications).

- MessagesConfig (messages.json)
  - purpose: Centralizes in-game messages, prefixes, and localization strings.

How QUIPT Loads Configs (Paper)
- Initializer.Quipt.registerConfigs() registers all config classes with ConfigManager.
- Files are saved if they do not exist; edits persist across restarts.
- On enable(), configs are retrieved and used to start subsystems:
  - WebConfig => QuiptServer
  - ResourceConfig => ResourcePackHandler @ /resources
  - DiscordConfig => Discord bot bootstrap

Webhook Endpoints (when WebConfig.enable = true)
- Resource Pack: http://<host>:<port>/resources/update/ (exact path may vary with handler route). Configure your VCS (e.g., GitHub) webhook to invoke this on push.

Security Tips
- Bind to localhost or your internal IP if you do not need public access.
- Use a reverse proxy (NGINX/Apache/Caddy) if exposing endpoints to the internet.
- Keep tokens (Discord, API keys) private and out of version control.
