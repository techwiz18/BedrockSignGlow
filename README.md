# BedrockSignGlow

A lightweight Minecraft plugin that makes all signs appear with glowing text for Bedrock/Floodgate players on Java servers.

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
