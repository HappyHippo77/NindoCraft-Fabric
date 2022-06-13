package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.sounds.ModSounds;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;

public class JutsuCaster {
    private final HashMap<IntArrayList, Object> handSignIndex = new HashMap<>();
    private static Object jutsuToCast;
    private static IntArrayList handSigns;
    private static PlayerEntity player;

    public JutsuCaster(PlayerEntity playerIn, IntArrayList handSignsIn) {
        handSignIndex.put(IntArrayList.of(1, 2, 3, 4), new TestJutsu(playerIn, playerIn.getRotationVector(), playerIn.getWorld(), playerIn.getEyePos()));
        handSigns = handSignsIn;
        player = playerIn;
    }

    private static void setJutsuToCast(IntArrayList jutsuHandSigns, Object jutsu) {
        if (handSigns.equals(jutsuHandSigns)) {
            jutsuToCast = jutsu;
        }
    }

    public void castJutsu() {

        handSignIndex.forEach(JutsuCaster::setJutsuToCast);

        if (jutsuToCast != null) {
            if (((NindoCraftPlayer) player).getChakra() >= ((Jutsu) jutsuToCast).getRequiredChakra()) {
                ((Jutsu) jutsuToCast).cast();
                player.getWorld().playSound(null, player.getBlockPos(), ModSounds.JUTSU_ACTIVATE, SoundCategory.PLAYERS,1f,1f);
            }
            else {
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS,1f,0.1f);
            }
        }

        jutsuToCast = null;

//        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) player;
//        TestJutsu testJutsu = new TestJutsu(player, player.getRotationVector(), player.getWorld(), player.getEyePos());
//        if (handSigns.equals(testJutsu.getHandSigns())) {
//            if (nindoCraftPlayer.getChakra() >= testJutsu.getRequiredChakra()) {
//                testJutsu.cast();
//                player.getWorld().playSound(null, player.getBlockPos(), ModSounds.JUTSU_ACTIVATE, SoundCategory.PLAYERS,1f,1f);
//            }
//            else {
//                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS,1f,0.1f);
//            }
//        }
    }

}