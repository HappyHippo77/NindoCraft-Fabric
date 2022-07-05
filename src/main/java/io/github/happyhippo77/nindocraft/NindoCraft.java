package io.github.happyhippo77.nindocraft;

import io.github.happyhippo77.nindocraft.networking.NindoCraftServerPackets;
import io.github.happyhippo77.nindocraft.sounds.ModSounds;
import io.github.happyhippo77.nindocraft.util.JutsuEntry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class NindoCraft implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "nindocraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int MAX_STAMINA_SCORE = 10;
	public static final int MAX_NINJUTSU_SCORE = 10;
	public static final int MAX_TAIJUTSU_SCORE = 10;
	public static final int MAX_GENJUTSU_SCORE = 10;
	public static ArrayList<JutsuEntry> jutsuIndex = new ArrayList<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		jutsuIndex.add(new JutsuEntry(IntArrayList.of(1, 2, 3, 4), "test_jutsu", 1, "ninjutsu"));

		NindoCraftServerPackets.initialize();

		ModSounds.registerSounds();
	}
}
