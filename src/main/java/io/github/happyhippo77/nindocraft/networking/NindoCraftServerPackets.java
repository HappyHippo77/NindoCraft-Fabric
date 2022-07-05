package io.github.happyhippo77.nindocraft.networking;

import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.sounds.ModSounds;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class NindoCraftServerPackets {
    static final Random random = new Random();

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.JUTSU_KEY_PRESSED, NindoCraftServerPackets::handleJutsuKeyPressed);
        ServerPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.ID_REQUESTED, NindoCraftServerPackets::handleIdRequested);
        ServerPlayNetworking.registerGlobalReceiver(NindoCraftPacketIdentifiers.STAT_BUTTON_PRESS, NindoCraftServerPackets::handleStatButtonPress);
    }

    public static void handleIdRequested(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) player;
        int exp = nindoCraftPlayer.getExp();
        int level = nindoCraftPlayer.getLevel();
        int nextLevel = nindoCraftPlayer.getNextLevel();
        int statPoints = nindoCraftPlayer.getStatPoints();
        int staminaScore = nindoCraftPlayer.getStaminaScore();
        int ninjutsuScore = nindoCraftPlayer.getNinjutsuScore();
        int genjutsuScore = nindoCraftPlayer.getGenjutsuScore();
        int taijutsuScore = nindoCraftPlayer.getTaijutsuScore();

        PacketByteBuf buf2 = PacketByteBufs.create();
        buf2.writeInt(exp);
        buf2.writeInt(level);
        buf2.writeInt(nextLevel);
        buf2.writeInt(statPoints);
        buf2.writeInt(staminaScore);
        buf2.writeInt(ninjutsuScore);
        buf2.writeInt(genjutsuScore);
        buf2.writeInt(taijutsuScore);

        server.execute(() -> {
            responseSender.sendPacket(NindoCraftPacketIdentifiers.ID_SENT, buf2);
        });
    }

    public static void handleJutsuKeyPressed(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) player;

        int key = buf.readInt();

        server.execute(() -> {
            if (key == -1) {
                if(nindoCraftPlayer.isHandSigning()) {
                    nindoCraftPlayer.setHandSignSequence(new HandSignSequence(new int[0]));
                    nindoCraftPlayer.setHandSigning(false);
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS,1f,0.1f);
                }
            }
            if (key == 0) {
                if(nindoCraftPlayer.isHandSigning()) {
                    nindoCraftPlayer.getHandSignSequence().cast(player);
                    nindoCraftPlayer.setHandSignSequence(new HandSignSequence(new int[0]));
                    nindoCraftPlayer.setHandSigning(false);
                }
            }
            if (key >= 1 && key <= 4) {
                if (nindoCraftPlayer.isHandSigning()) {
                    nindoCraftPlayer.getHandSignSequence().addHandSign(key);
                }
                else {
                    nindoCraftPlayer.setHandSignSequence(new HandSignSequence(new int[0]));
                    nindoCraftPlayer.setHandSigning(true);
                    nindoCraftPlayer.getHandSignSequence().addHandSign(key);
                }
                player.getWorld().playSound(null, player.getBlockPos(), ModSounds.HANDSIGN, SoundCategory.PLAYERS,1f,0.9f + random.nextFloat(0.2f));
            }
            if (key == 5) {
                if (!nindoCraftPlayer.isHandSigning()) {
                    if (nindoCraftPlayer.getStamina() > 0) {
                        nindoCraftPlayer.setRechargeStamina(false);
                        nindoCraftPlayer.setChakra(nindoCraftPlayer.getChakra() + 1);
                        nindoCraftPlayer.setStamina(nindoCraftPlayer.getStamina() - 1);
                    }
                }
            }
            if (key == -2) {
                nindoCraftPlayer.setRechargeStamina(true);
            }
        });
    }

    public static void handleStatButtonPress(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) player;

        int stat = buf.readInt();

        if (stat == 0) {
            if (nindoCraftPlayer.getStaminaScore() < NindoCraft.MAX_STAMINA_SCORE && nindoCraftPlayer.getStatPoints() > 0) {
                nindoCraftPlayer.setStaminaScore(nindoCraftPlayer.getStaminaScore() + 1);
                nindoCraftPlayer.setStatPoints(nindoCraftPlayer.getStatPoints() - 1);
                responseSender.sendPacket(NindoCraftPacketIdentifiers.STAT_BUTTON_RESPONSE, PacketByteBufs.empty());
            }
        }
        if (stat == 1) {
            if (nindoCraftPlayer.getNinjutsuScore() < NindoCraft.MAX_NINJUTSU_SCORE && nindoCraftPlayer.getStatPoints() > 0) {
                nindoCraftPlayer.setNinjutsuScore(nindoCraftPlayer.getNinjutsuScore() + 1);
                nindoCraftPlayer.setStatPoints(nindoCraftPlayer.getStatPoints() - 1);
                responseSender.sendPacket(NindoCraftPacketIdentifiers.STAT_BUTTON_RESPONSE, PacketByteBufs.empty());
            }
        }
        if (stat == 2) {
            if (nindoCraftPlayer.getTaijutsuScore() < NindoCraft.MAX_TAIJUTSU_SCORE && nindoCraftPlayer.getStatPoints() > 0) {
                nindoCraftPlayer.setTaijutsuScore(nindoCraftPlayer.getTaijutsuScore() + 1);
                nindoCraftPlayer.setStatPoints(nindoCraftPlayer.getStatPoints() - 1);
                responseSender.sendPacket(NindoCraftPacketIdentifiers.STAT_BUTTON_RESPONSE, PacketByteBufs.empty());
            }
        }
        if (stat == 3) {
            if (nindoCraftPlayer.getGenjutsuScore() < NindoCraft.MAX_GENJUTSU_SCORE && nindoCraftPlayer.getStatPoints() > 0) {
                nindoCraftPlayer.setGenjutsuScore(nindoCraftPlayer.getGenjutsuScore() + 1);
                nindoCraftPlayer.setStatPoints(nindoCraftPlayer.getStatPoints() - 1);
                responseSender.sendPacket(NindoCraftPacketIdentifiers.STAT_BUTTON_RESPONSE, PacketByteBufs.empty());
            }
        }
    }

    public static void sendLevelUp(PlayerEntity player) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, NindoCraftPacketIdentifiers.LEVEL_UP, PacketByteBufs.empty());
    }

    public static void sendRenderPlayerStatsPacket(NindoCraftPlayer nindoCraftPlayer) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(nindoCraftPlayer.getChakra());
        buf.writeInt(nindoCraftPlayer.getStamina());
        buf.writeIntList(nindoCraftPlayer.getHandSignSequence().getHandSigns());
        ServerPlayNetworking.send((ServerPlayerEntity) nindoCraftPlayer, NindoCraftPacketIdentifiers.RENDER_PLAYER_STATS, buf);
    }

    public static void sendRenderParticle(PlayerEntity player, ParticleEffect particleEffect, float x, float y, float z, float xd, float yd, float zd) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeVarInt(Registry.PARTICLE_TYPE.getRawId(particleEffect.getType()));
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(xd);
        buf.writeFloat(yd);
        buf.writeFloat(zd);

        ServerPlayNetworking.send((ServerPlayerEntity) player, NindoCraftPacketIdentifiers.RENDER_PARTICLE, buf);
    }
}
