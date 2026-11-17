
Player data and persistence
- Moved player data and persistence to the individual integration modules.
- Changed metadata field to JSONObject class instead of Metadata

Versioning
- Changed build number to depend on Jenkins build number

Paper Module
- Implement assembler command and definition loader for structure generation
- Updated lifecycle to register listeners and commands after enabling the integration

Configs
- Psuedo-abstracted config system to allow for easier config management and loading