package io.github.happyhippo77.nindocraft.networking;

import io.github.happyhippo77.nindocraft.NindoCraft;
import net.minecraft.util.Identifier;

public class NindoCraftPacketIdentifiers {
    public static final Identifier JUTSU_KEY_PRESSED = new Identifier(NindoCraft.MOD_ID, "jutsu_key_pressed");
    public static final Identifier ID_REQUESTED = new Identifier(NindoCraft.MOD_ID, "id_requested");
    public static final Identifier ID_SENT = new Identifier(NindoCraft.MOD_ID, "id_sent");
    public static final Identifier STAT_BUTTON_PRESS = new Identifier(NindoCraft.MOD_ID, "stat_button_press");
    public static final Identifier STAT_BUTTON_RESPONSE = new Identifier(NindoCraft.MOD_ID, "stat_button_response");
    public static final Identifier LEVEL_UP = new Identifier(NindoCraft.MOD_ID, "level_up");
    public static final Identifier RENDER_PLAYER_STATS = new Identifier(NindoCraft.MOD_ID, "render_player_stats");
    public static final Identifier RENDER_PARTICLE = new Identifier(NindoCraft.MOD_ID, "render_particle");
}
