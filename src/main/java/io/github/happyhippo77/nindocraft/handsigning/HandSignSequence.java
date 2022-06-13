package io.github.happyhippo77.nindocraft.handsigning;

import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.jutsu.JutsuCaster;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.player.PlayerEntity;

public class HandSignSequence {
    private final IntArrayList handSigns;
    public HandSignSequence(int[] handSigns) {
        this.handSigns = IntArrayList.of(handSigns);
    }

    public void addHandSign(int sign) {
        this.handSigns.add(sign);
    }

    public IntArrayList getHandSigns() {
        return this.handSigns;
    }

    public void cast(PlayerEntity player) {
        JutsuCaster jc = new JutsuCaster(player, this.handSigns);
        jc.castJutsu();
    }
}
