package io.github.happyhippo77.nindocraft.jutsu;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Jutsu {
    private PlayerEntity caster;
    private int exp;
    private int chakra;
    private ArrayList<Integer> handSigns;

    public Jutsu(PlayerEntity caster, int exp, int chakra, ArrayList<Integer> handSigns) {
        this.caster = caster;
        this.exp = exp;
        this.handSigns = handSigns;
        this.chakra = chakra;
    }

    public PlayerEntity getCaster() {
        return caster;
    }

    public int getExp() {
        return exp;
    }

    public int getChakra() {
        return chakra;
    }

    public ArrayList<Integer> getHandSigns() {
        return handSigns;
    }

    public boolean cast() {
        return true;
    }
}
