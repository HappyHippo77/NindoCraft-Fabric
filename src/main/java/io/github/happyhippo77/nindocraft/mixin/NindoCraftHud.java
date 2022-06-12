package io.github.happyhippo77.nindocraft.mixin;

import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.round;

@Mixin(InGameHud.class)
public abstract class NindoCraftHud {
    private int chakra;
    private int stamina;
    private int staminaScore;
    private int maxStamina;
    private String staminaString;
    private String chakraString;

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient minecraftClient, ItemRenderer itemRenderer, CallbackInfo ci) {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "player_stats_to_renderer"), (client, handler, buf, responseSender) -> {
            this.chakra = buf.readInt();
            this.stamina = buf.readInt();
            this.staminaScore = buf.readInt();
            this.maxStamina = buf.readInt();
        });
    }

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void onRender (MatrixStack matrices, float tickDelta, CallbackInfo info) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String slash = "/";
        this.chakraString = String.valueOf(this.chakra);
        this.staminaString = String.valueOf(this.stamina);

        int center = scaledWidth / 2;
        textRenderer.draw(matrices, slash, center - (float) textRenderer.getWidth(slash) / 2, this.scaledHeight - 39 - textRenderer.fontHeight - 8, -1);
        textRenderer.draw(matrices, this.chakraString, center - textRenderer.getWidth(chakraString) - (float) textRenderer.getWidth(slash) / 2, this.scaledHeight - 39 - textRenderer.fontHeight - 8, 43690);
        textRenderer.draw(matrices, this.staminaString, center + (float) textRenderer.getWidth(slash) / 2, this.scaledHeight - 39 - textRenderer.fontHeight - 8, 11141290);
    }
}
