package io.github.happyhippo77.nindocraft.handsigning;

import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.jutsu.JutsuCaster;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

public class HandSignSequence {
    private ArrayList<Integer> handSigns;
    public HandSignSequence(Integer[] handSigns) {
        this.handSigns = new ArrayList<>(Arrays.asList(handSigns));
    }

    public void addHandSign(int sign) {
        this.handSigns.add(sign);
        System.out.println("handSigns = " + handSigns);
    }

    public ArrayList<Integer> getHandSigns() {
        return handSigns;
    }

    public void cast(PlayerEntity player) {
        System.out.println("Casting...");
        JutsuCaster jc = new JutsuCaster();
        jc.castJutsu(player, this.handSigns);
    }
}
