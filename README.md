# BedrockSignGlow

A lightweight Minecraft plugin that makes all signs appear with glowing text for Bedrock/Floodgate players on Java servers.

## About

On hybrid Java/Bedrock servers running [Geyser](https://geysermc.org/) and [Floodgate](https://geysermc.org/download/floodgate), sign text readability is a real problem for Bedrock players.

Java clients render colored sign text with vibrant, high-contrast colors that are easy to read. Bedrock clients, however, have noticeably lower saturation and different text rendering — colored signs that look perfectly fine on Java become washed out and nearly impossible to read on Bedrock. The only workaround is applying a Glow Ink Sac to signs, which adds the dark outline background that makes text readable. But that also forces the glowing effect on Java clients, which may not fit the server's intended look.

BedrockSignGlow solves this by forcing the glowing text effect on all signs — but only for Bedrock players. Java clients are completely unaffected. Server owners get readable signs for Bedrock without having to apply Glow Ink Sacs, which would force the glowing aesthetic on Java players where it may not fit the server's look.

This is especially useful for servers that want readable signs for all players without forcing the glowing aesthetic on Java clients.

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
