# BedrockSignGlow

A lightweight Minecraft plugin that makes all signs appear with glowing text for Bedrock/Floodgate players on Java servers.

## About

On hybrid Java/Bedrock servers running [Geyser](https://geysermc.org/) and [Floodgate](https://geysermc.org/download/floodgate), Bedrock clients connect through a proxy that translates the Java protocol. One thing this translation misses: sign glowing text.

When a Java player dyes a sign and makes the text glow, Bedrock players see the sign as plain — the glowing effect is silently dropped during protocol translation. This happens because the `has_glowing_text` flag in the sign's NBT data isn't being forwarded correctly to Bedrock clients.

BedrockSignGlow fixes this by intercepting outgoing sign packets and forcing the `has_glowing_text` flag for Bedrock players. The result is that Bedrock clients render signs with the same glowing effect Java players see — the bright text with the dark outline background.

This is especially useful for servers that use glowing signs for navigation, shops, rules, or any other purpose where readability matters for all players regardless of platform.

## Features

- **Zero configuration** — install and forget
- **All signs glow** — every sign automatically renders with glowing text for Bedrock clients
- **Java clients unaffected** — only Bedrock players see the glow
- **No performance impact** — intercepts existing packets, no extra network traffic

## Requirements

- PaperMC 1.20+ (or compatible fork)
- [ProtocolLib](https://www.spigotmc.org/wiki/protocollib/) 5.4.0+
- [Floodgate](https://geysermc.org/download/floodgate) 2.2.3+

## Installation

1. Download the latest JAR from [Releases](https://github.com/techwiz18/BedrockSignGlow/releases)
2. Place it in your server's `plugins/` folder alongside ProtocolLib and Floodgate
3. Restart the server

## How it works

The plugin intercepts two types of outgoing packets:

- **`TILE_ENTITY_DATA`** — catches signs updated at runtime (placing, editing)
- **`MAP_CHUNK`** — catches signs loaded with chunks (on join, walking into new areas)

For each sign sent to a Bedrock player, it sets `has_glowing_text` to `1` in the sign's NBT data. Java clients continue to see signs normally since the plugin only modifies packets sent to Floodgate players.

## Building

```bash
git clone https://github.com/techwiz18/BedrockSignGlow.git
cd BedrockSignGlow
mvn clean package
```

The built JAR will be in `target/`.

## License

MIT License — see [LICENSE](LICENSE) for details.
