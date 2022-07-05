package io.github.happyhippo77.nindocraft.networking;

import io.github.happyhippo77.nindocraft.client.NindoCraftIdGui;
import io.github.happyhippo77.nindocraft.client.NindoCraftIdScreen;
import io.github.happyhippo77.nindocraft.util.NindoCraftHud;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.Registry;

public class NindoCraftClientPackets {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.RENDER_PLAYER_STATS, NindoCraftClientPackets::handleRenderPlayerStats);
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.RENDER_PARTICLE, NindoCraftClientPackets::handleRenderParticle);
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.ID_SENT, NindoCraftClientPackets::handleIdSent);
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.STAT_BUTTON_RESPONSE, NindoCraftClientPackets::handleStatButtonResponse);
        ClientPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.LEVEL_UP, NindoCraftClientPackets::handleLevelUp);
    }

    public static void sendJutsuKeyPressed(int key) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(key);
        ClientPlayNetworking.send(NindoCraftPacketIdentifiers.JUTSU_KEY_PRESSED, buf);
    }

    public static void sendIdRequested() {
        ClientPlayNetworking.send(NindoCraftPacketIdentifiers.ID_REQUESTED, PacketByteBufs.empty());
    }

    public static void sendStatButtonPress(int stat) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(stat);
        ClientPlayNetworking.send(NindoCraftPacketIdentifiers.STAT_BUTTON_PRESS, buf);
    }

    public static void handleIdSent(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int exp = buf.readInt();
        int level = buf.readInt();
        int nextLevel = buf.readInt();
        int statPoints = buf.readInt();
        int staminaScore = buf.readInt();
        int ninjutsuScore = buf.readInt();
        int genjutsuScore = buf.readInt();
        int taijutsuScore = buf.readInt();

        client.execute(() -> {
            client.setScreen(new NindoCraftIdScreen(new NindoCraftIdGui(exp, level, nextLevel, statPoints, staminaScore, ninjutsuScore, genjutsuScore, taijutsuScore)));
        });
    }

    public static void handleStatButtonResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.world.playSound(client.player, client.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        NindoCraftClientPackets.sendIdRequested();
    }

    public static void handleLevelUp(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.world.playSound(client.player, client.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
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
