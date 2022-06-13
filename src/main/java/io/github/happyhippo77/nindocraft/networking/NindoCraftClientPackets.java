package io.github.happyhippo77.nindocraft.networking;

import io.github.happyhippo77.nindocraft.util.NindoCraftHud;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.registry.Registry;

public class NindoCraftClientPackets {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.RENDER_PLAYER_STATS, NindoCraftClientPackets::handleRenderPlayerStats);
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.RENDER_PARTICLE, NindoCraftClientPackets::handleRenderParticle);
    }

    public static void sendJutsuKeyPressed(int key) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(key);
        ClientPlayNetworking.send(NindoCraftPacketIdentifiers.JUTSU_KEY_PRESSED, buf);
    }

    public static void handleRenderPlayerStats(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NindoCraftHud hud = (NindoCraftHud) client.inGameHud;
        int chakra = buf.readInt();
        int stamina = buf.readInt();
        IntArrayList handSigns = (IntArrayList) buf.readIntList();

        client.execute(() -> {
           hud.setChakraText(String.valueOf(chakra));
            hud.setStaminaText(String.valueOf(stamina));
            hud.setHandSigns(handSigns);
        });
    }
    public static void handleRenderParticle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readVarInt();
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();
        float xd = buf.readFloat();
        float yd = buf.readFloat();
        float zd = buf.readFloat();

        ParticleEffect particleEffect = (ParticleEffect) Registry.PARTICLE_TYPE.get(id);

        client.execute(() -> {
            client.world.addParticle(particleEffect, x, y, z, xd, yd, zd);
        });
    }
}
