package io.github.happyhippo77.nindocraft.networking;

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
