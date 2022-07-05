package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.sounds.ModSounds;
import io.github.happyhippo77.nindocraft.util.JutsuEntry;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;

public class JutsuCaster {
    private static PlayerEntity player;
    public static void check(IntArrayList handSigns, ServerPlayerEntity playerEntity) {
        player = playerEntity;

        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) player;

        for (JutsuEntry entry : NindoCraft.jutsuIndex) {
            if (entry.getHandSigns().equals(handSigns)) {
                if (nindoCraftPlayer.getKnownJutsu().contains(entry.name)) {
                    cast(entry.name);
                }
            }
        }
    }

    private static void cast(String name) {
        switch (name) {
            case "test_jutsu":
                new TestJutsu(player, player.getRotationVector(), player.getWorld(), player.getEyePos());
                break;
        }
    }

}