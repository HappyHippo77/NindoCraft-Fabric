package io.github.happyhippo77.nindocraft.mixin;

import io.github.happyhippo77.nindocraft.util.NindoCraftHud;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class NindoCraftHudMixin implements NindoCraftHud {
    private String chakraText;
    private String staminaText;

    private IntArrayList handSigns = IntArrayList.of();

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;
    @Override
    public void setChakraText(String text) {
        this.chakraText = text;
    }

    @Override
    public void setStaminaText(String text) {
        this.staminaText = text;
    }

    @Override
    public void setHandSigns(IntArrayList handSigns) {
        this.handSigns = handSigns;
    }

    final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender (MatrixStack matrices, float tickDelta, CallbackInfo info) {
        String separator = "/";
        StringBuilder handSignsText = new StringBuilder();

        for (int i = 0; i < handSigns.size(); i++) {
            handSignsText.append(handSigns.getInt(i));
            if (!(i + 1 >= handSigns.size())) {
                handSignsText.append("-");
            }
        }

        int center = scaledWidth / 2;
        int y = this.scaledHeight - 39 - textRenderer.fontHeight - 8;
        textRenderer.draw(matrices, separator, center - (float) textRenderer.getWidth(separator) / 2, y, -1);
        textRenderer.draw(matrices, this.chakraText, center - textRenderer.getWidth(chakraText) - (float) textRenderer.getWidth(separator) / 2, y, 43690);
        textRenderer.draw(matrices, this.staminaText, center + (float) textRenderer.getWidth(separator) / 2, y, 11141290);

        if (handSigns.size() > 0) {
            y = y - textRenderer.fontHeight - 1;
            textRenderer.draw(matrices, handSignsText.toString(), center - (float) textRenderer.getWidth(handSignsText.toString()) / 2, y, -1);
        }
    }

}
