# Unreleased (2025-09-28)

## Highlights
- NetworkUtils: added binary download support via generic GET with BodyHandlers.ofInputStream and a save helper to write streams to disk.
- NetworkUtils: POST/PUT methods are now null-safe for the JSON body (null sends an empty body). This avoids NullPointerExceptions and aligns with common API expectations.
- NetworkUtils: expanded Javadoc across methods to better document configuration, timeouts, and body handling.
- QueueManager/Queue: introduced as experimental utilities. These are non-functional stubs intended for preview; APIs are unstable and subject to change.

## Implementation Guide (How to update your code)
- Downloading images/files (recommended):
  ```java
  // Using a custom HttpConfig if needed
  HttpConfig config = HttpConfig.config(Duration.ofSeconds(10), 3000, false, false, "image/png", "UTF-8");
  HttpResponse<InputStream> resp = NetworkUtils.get(config, "https://example.com/image.png", HttpResponse.BodyHandlers.ofInputStream());
  NetworkUtils.save(resp, new File("./image.png"));

  // Or, with the default config
  HttpResponse<InputStream> resp2 = NetworkUtils.get(NetworkUtils.DEFAULT, "https://example.com/file.bin", HttpResponse.BodyHandlers.ofInputStream());
  NetworkUtils.save(resp2, new File("./file.bin"));
  ```
- Existing GET/POST usage continues to work without changes. If you previously handled only String responses, you can keep doing so with BodyHandlers.ofString(). Only add the InputStream handler when you need binary data.
- POST/PUT with optional body:
  ```java
  // body may be null – an empty request body will be sent instead of throwing
  HttpResponse<String> r = NetworkUtils.post(NetworkUtils.DEFAULT, "https://api.example.com/endpoint", null);
  ```
- QueueManager/Queue (experimental):
  - These components are stubs and currently non-functional.
  - No migration or code changes are required at this time.
  - Do not rely on them in production; APIs may change or be removed.

## Compatibility
- This update is not API-breaking. All existing public methods retain their signatures and behavior; the binary-download pattern is additive through BodyHandlers.
- Safe to backport: these changes can be applied on top of the previous release line without breaking existing integrations.

---

# Version 1.0.8-MC1.21.8-alpha.1-SS.5

## Build Log
### BUILD 1-SS.5
#### Embeds
Webhook Embeds must now be built using the `Embed.Builder` class. The old `Embed` class has been changed to remove its constructor and now has a static `builder` method that returns a new `Embed.Builder` instance.

BEFORE:
```java
Embed embed = new Embed()
    .title("Title")
    .description("Description")
    .color(0xFF0000);
```

AFTER:
```java
Embed embed = Embed.builder()
    .title("Title")
    .description("Description")
    .color(0xFF0000)
    .build();
```

Embeds can now accept java.awt.Color objects for setting colors.