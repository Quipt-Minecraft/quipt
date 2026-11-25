
Player data and persistence
- Changed metadata field to JSONObject class instead of Metadata

Versioning
- Changed build number to depend on Jenkins build number

Paper Module
- Implement assembler command and definition loader for structure generation
- Updated lifecycle to register listeners and commands after enabling the integration
- Fixed backwards initialization errors with QuiptCore built in

Common Module
- Created "CommandData" class so command arguments don't have to be "commands" and register new permission registeries

Configs
- Psuedo-abstracted config system to allow for easier config management and loading