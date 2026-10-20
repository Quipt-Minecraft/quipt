Player data and persistence
- Added `PlayerConfig` to represent persisted player data and supply a default player ID.
- Refactored Paper player registration to load and save player configurations through the integration’s configuration system.
- Registered the player collection as a config value so changes are actually persisted.
- Introduced a player factory and refactored `PlayerManager`/`PaperPlayers` to centralize player creation and management.
- Updated player lookup to use the configured player ID instead of storing a separate UUID on `PaperPlayer`.
- Added JSON initialization support for player models and their metadata.
- Improved UUID handling in `PlayerManager` by using `Optional`.

Metadata and serialization
- Implemented `JsonSerializable` for `Metadata`.
- Initialize metadata when creating a `MinecraftPlayer`, expose it for consumers, and serialize it with the player data.
- Added metadata deserialization to the Minecraft and Paper player JSON constructors.

Paper plugin structure and fixes
- Refactored `QuiptPlugin` to provide `listeners()` and `commands()` extension points, and moved `PaperPlayers` initialization into the base plugin class.
- Fixed the team ID check in `ExtraLifePlatform`.
- Updated `PaperPlayers` to use the integration configuration and clarified how it retrieves `PlayerConfig`.
- Registered `PartyCommand` with the command system.

Tests
- Added Mockito as a test dependency.
- Added unit tests covering metadata serialization and the Paper player lifecycle.

Build and publishing
- Updated the project build number to 27.
- Improved Modrinth upload logging to include version details after the task completes.
- Updated the upload webhook payload structure.
- Consolidated Jenkins configuration and added the Discord update channel and token to the pipeline environment.