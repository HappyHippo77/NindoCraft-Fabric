package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.io.Serializable;

public abstract class Jutsu implements Serializable {
    private final PlayerEntity caster;
    private final int exp;
    private final int chakra;
    private final IntArrayList handSigns;

    public Jutsu(PlayerEntity caster, int exp, int chakra, IntArrayList handSigns) {
        this.caster = caster;
        this.exp = exp;
        this.chakra = chakra;
        this.handSigns = handSigns;
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
    }

    public void serverTick(MinecraftServer server) {
    }
}
