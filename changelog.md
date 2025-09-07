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