package io.github.happyhippo77.nindocraft;

import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class NindoCraftClient implements ClientModInitializer {

    private static KeyBinding keyHandSign1;
    private static KeyBinding keyHandSign2;
    private static KeyBinding keyHandSign3;
    private static KeyBinding keyHandSign4;
    private static KeyBinding keyCast;
    private boolean isHandSigning;

    private void addHandSignPacket(int i) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(i);
        ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "add_handsign_to_player"), buf);
    }

    @Override
    public void onInitializeClient() {
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

        ServerPlayConnectionEvents.JOIN.register((networkHandler, packetSender, server) -> {
            ClientPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "handsigns_to_key_handler"), (client, handler, buf, responseSender) -> {
                this.isHandSigning = buf.readBoolean();
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyCast.wasPressed()) {
                if (isHandSigning) {
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "cast_jutsu_from_client"), PacketByteBufs.empty());
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "end_handsign_sequence"), PacketByteBufs.empty());
                }
                //TestJutsu jutsu = new TestJutsu(client.player, client.player.getRotationVector(), (float) client.player.getEyePos().x, (float) client.player.getEyePos().y, (float) client.player.getEyePos().z);
            }
            while (keyHandSign1.wasPressed()) {
                if (isHandSigning) {
                    addHandSignPacket(1);
                }
                else {
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "start_handsign_sequence"), PacketByteBufs.empty());
                    addHandSignPacket(1);
                }
            }
            while (keyHandSign2.wasPressed()) {
                if (isHandSigning) {
                    addHandSignPacket(2);
                }
                else {
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "start_handsign_sequence"), PacketByteBufs.empty());
                    addHandSignPacket(2);
                }
            }
            while (keyHandSign3.wasPressed()) {
                if (isHandSigning) {
                    addHandSignPacket(3);
                }
                else {
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "start_handsign_sequence"), PacketByteBufs.empty());
                    addHandSignPacket(3);
                }
            }
            while (keyHandSign4.wasPressed()) {
                if (isHandSigning) {
                    addHandSignPacket(4);
                }
                else {
                    ClientPlayNetworking.send(new Identifier(NindoCraft.MOD_ID, "start_handsign_sequence"), PacketByteBufs.empty());
                    addHandSignPacket(4);
                }
            }
        });
    }
}
