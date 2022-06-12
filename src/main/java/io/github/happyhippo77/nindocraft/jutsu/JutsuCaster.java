package io.github.happyhippo77.nindocraft.jutsu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

public class JutsuCaster {
    public void castJutsu(PlayerEntity player, ArrayList<Integer> handSigns) {
        TestJutsu testJutsu = new TestJutsu(player, player.getRotationVector(), player.getEyePos());
        System.out.println(testJutsu.getHandSigns());
        if (handSigns.equals(testJutsu.getHandSigns())) {
            testJutsu.cast();
        }
    }

}