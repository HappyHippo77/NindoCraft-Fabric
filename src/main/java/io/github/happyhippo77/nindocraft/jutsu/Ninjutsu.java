package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.sounds.ModSounds;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.io.Serializable;

public abstract class Ninjutsu implements Serializable {
    private final PlayerEntity caster;
    private final NindoCraftPlayer nindoCraftPlayer;
    private final int exp;
    private final int chakra;
    private final IntArrayList handSigns;

    public Ninjutsu(PlayerEntity caster, int exp, int chakra, IntArrayList handSigns) {
        this.caster = caster;
        this. nindoCraftPlayer = (NindoCraftPlayer) caster;
        this.exp = exp;
        this.chakra = chakra;
        this.handSigns = handSigns;

        if (nindoCraftPlayer.getChakra() >= this.chakra) {
            cast();
            caster.getWorld().playSound(null, caster.getBlockPos(), ModSounds.JUTSU_ACTIVATE, SoundCategory.PLAYERS, 1f, 1f);
        } else {
            caster.getWorld().playSound(null, caster.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1f, 0.1f);
        }
    }

    public PlayerEntity getCaster() {
        return caster;
    }

    public int getExp() {
        return exp;
    }

    public int getRequiredChakra() {
        return chakra;
    }

    public IntArrayList getHandSigns() {
        return handSigns;
    }

    public void cast() {
        NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) caster;
        nindoCraftPlayer.setChakra(nindoCraftPlayer.getChakra() - this.getRequiredChakra());
        nindoCraftPlayer.addExp(this.getExp());
    }

    public void serverTick(MinecraftServer server) {
    }
}
