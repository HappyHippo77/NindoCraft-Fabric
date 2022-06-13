package io.github.happyhippo77.nindocraft;

import io.github.happyhippo77.nindocraft.networking.NindoCraftClientPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NindoCraftClient implements ClientModInitializer {

    private static KeyBinding keyHandSign1;
    private static KeyBinding keyHandSign2;
    private static KeyBinding keyHandSign3;
    private static KeyBinding keyHandSign4;
    private static KeyBinding keyCast;
    private static KeyBinding keyCharge;
    private static KeyBinding keyCancel;

    private static void tick(MinecraftClient client) {

        if (client.world != null) {
            if (!keyCharge.wasPressed()) {
                NindoCraftClientPackets.sendJutsuKeyPressed(-2);
            }
        }

        while (keyCancel.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(-1);
        }
        while (keyCast.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(0);
        }
        while (keyCharge.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(5);
        }
        while (keyHandSign1.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(1);
        }
        while (keyHandSign2.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(2);
        }
        while (keyHandSign3.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(3);
        }
        while (keyHandSign4.wasPressed()) {
            NindoCraftClientPackets.sendJutsuKeyPressed(4);
        }
    }

    @Override
    public void onInitializeClient() {
        NindoCraftClientPackets.initialize();

        keyHandSign1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.handsign1", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_C, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyHandSign2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.handsign2", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_V, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyHandSign3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.handsign3", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_B, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyHandSign4 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.handsign4", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_N, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyCast = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.cast", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyCancel = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.cancel", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_G, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));
        keyCharge = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nindocraft.charge", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_LEFT_ALT, // The keycode of the key
                "category.nindocraft.main" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(NindoCraftClient::tick);
    }
}
