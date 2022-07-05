package io.github.happyhippo77.nindocraft.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class JutsuEntry {
    public IntArrayList handSigns;
    public String name;
    public int level;
    public String type;

    public JutsuEntry(IntArrayList handSigns, String name, int level, String type) {
        this.handSigns = handSigns;
        this.name = name;
        this.level = level;
        this.type = type;
    }

    public IntList getHandSigns() {
        return handSigns;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }
}
