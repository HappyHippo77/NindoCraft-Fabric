package io.github.happyhippo77.nindocraft.sounds;

import io.github.happyhippo77.nindocraft.NindoCraft;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final Identifier HANDSIGN_ID = new Identifier(NindoCraft.MOD_ID, "handsign");
    public static final SoundEvent HANDSIGN = new SoundEvent(HANDSIGN_ID);

    public static final Identifier JUTSU_ACTIVATE_ID = new Identifier(NindoCraft.MOD_ID, "jutsu_activate");
    public static final SoundEvent JUTSU_ACTIVATE = new SoundEvent(JUTSU_ACTIVATE_ID);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, HANDSIGN_ID, HANDSIGN);
        Registry.register(Registry.SOUND_EVENT, JUTSU_ACTIVATE_ID, JUTSU_ACTIVATE);
    }
}
