package io.github.happyhippo77.nindocraft.client;

import io.github.happyhippo77.nindocraft.networking.NindoCraftClientPackets;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;

public class AddStatButtonRunnable implements Runnable {
    public final int stat;

    public AddStatButtonRunnable(int stat) {
        this.stat = stat;
    }

    @Override
    public void run() {
        NindoCraftClientPackets.sendStatButtonPress(this.stat);
    }
}
