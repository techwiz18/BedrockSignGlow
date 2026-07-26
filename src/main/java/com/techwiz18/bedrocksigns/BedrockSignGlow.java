package com.techwiz18.bedrocksigns;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.comphenix.protocol.wrappers.WrappedLevelChunkData.BlockEntityInfo;
import com.comphenix.protocol.wrappers.WrappedLevelChunkData.ChunkData;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class BedrockSignGlow extends JavaPlugin {

    private static final Set<MinecraftKey> SIGN_TYPES = Set.of(
            new MinecraftKey("sign"),
            new MinecraftKey("hanging_sign")
    );

    @Override
    public void onEnable() {
        if (!validateDependencies()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerPacketListeners();
        getLogger().info("BedrockSignGlow enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedrockSignGlow disabled.");
    }

    private boolean validateDependencies() {
        try {
            ProtocolLibrary.getProtocolManager();
        } catch (Exception e) {
            getLogger().severe("ProtocolLib not found!");
            return false;
        }

        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
        } catch (ClassNotFoundException e) {
            getLogger().severe("Floodgate API not found!");
            return false;
        }

        return true;
    }

    private void registerPacketListeners() {
        ProtocolManager mgr = ProtocolLibrary.getProtocolManager();

        mgr.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                PacketType.Play.Server.TILE_ENTITY_DATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!isFloodgate(event)) return;

                try {
                    NbtBase<?> raw = event.getPacket().getNbtModifier().readSafely(0);
                    if (!(raw instanceof NbtCompound)) return;

                    NbtCompound root = (NbtCompound) raw;
                    if (applyGlow(root)) {
                        event.getPacket().getNbtModifier().write(0, root);
                    }
                } catch (Exception e) {
                    getLogger().warning("TILE_ENTITY_DATA error: " + e.getMessage());
                }
            }
        });

        mgr.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                PacketType.Play.Server.MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!isFloodgate(event)) return;

                try {
                    ChunkData chunkData = new ChunkData(
                            event.getPacket().getStructures().read(0).getHandle());

                    List<BlockEntityInfo> blockEntities = chunkData.getBlockEntityInfo();
                    boolean changed = false;

                    for (BlockEntityInfo info : blockEntities) {
                        MinecraftKey type = info.getTypeKey();
                        if (type == null || !SIGN_TYPES.contains(type)) continue;

                        NbtCompound nbt = info.getAdditionalData();
                        if (nbt != null && applyGlow(nbt)) {
                            info.setAdditionalData(nbt);
                            changed = true;
                        }
                    }

                    if (changed) {
                        chunkData.setBlockEntityInfo(blockEntities);
                    }
                } catch (Exception e) {
                    getLogger().warning("MAP_CHUNK error: " + e.getMessage());
                }
            }
        });
    }

    private boolean applyGlow(NbtCompound root) {
        return setGlowing(root, "front_text") | setGlowing(root, "back_text");
    }

    private boolean setGlowing(NbtCompound root, String key) {
        if (!root.containsKey(key)) return false;
        try {
            NbtBase<?> val = root.getValue(key);
            if (val instanceof NbtCompound) {
                ((NbtCompound) val).put("has_glowing_text", (byte) 1);
                return true;
            }
        } catch (Exception e) {
            getLogger().warning("Failed to set glow on '" + key + "': " + e.getMessage());
        }
        return false;
    }

    private boolean isFloodgate(PacketEvent event) {
        try {
            org.geysermc.floodgate.api.FloodgateApi api =
                    org.geysermc.floodgate.api.FloodgateApi.getInstance();
            return api != null && api.isFloodgatePlayer(event.getPlayer().getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }
}
