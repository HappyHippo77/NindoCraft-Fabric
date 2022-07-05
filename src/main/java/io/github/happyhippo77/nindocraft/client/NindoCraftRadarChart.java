package io.github.happyhippo77.nindocraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.happyhippo77.nindocraft.NindoCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import static io.github.happyhippo77.nindocraft.client.NindoCraftIdGui.*;

public class NindoCraftRadarChart extends WWidget {
    private final int staminaScore;
    private final int ninjutsuScore;
    private final int taijutsuScore;
    private final int genjutsuScore;

    public NindoCraftRadarChart(int staminaScore, int ninjutsuScore, int taijutsuScore, int genjutsuScore) {
        this.staminaScore = staminaScore;
        this. ninjutsuScore = ninjutsuScore;
        this.taijutsuScore = taijutsuScore;
        this.genjutsuScore = genjutsuScore;
    }

    @Override
    public boolean canResize() {
        return true; // set to false if you want a static size
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        float staminaScorePercent = staminaScore / (float) NindoCraft.MAX_STAMINA_SCORE;
        float ninjutsuScorePercent = ninjutsuScore / (float) NindoCraft.MAX_NINJUTSU_SCORE;
        float taijutsuScorePercent = taijutsuScore / (float) NindoCraft.MAX_TAIJUTSU_SCORE;
        float genjutsuScorePercent = genjutsuScore / (float) NindoCraft.MAX_GENJUTSU_SCORE;

        if (staminaScorePercent == 0) {
            staminaScorePercent = 0.05f;
        }
        if (ninjutsuScorePercent == 0) {
            ninjutsuScorePercent = 0.05f;
        }
        if (taijutsuScorePercent == 0) {
            taijutsuScorePercent = 0.05f;
        }
        if (genjutsuScorePercent == 0) {
            genjutsuScorePercent = 0.05f;
        }

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        ScreenDrawing.texturedRect(matrices, x, y, width, height, new Identifier(NindoCraft.MOD_ID, "textures/gui/id_stats_background.png"), 0xFFFFFFFF);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float) x + width / 2f, (float) y + (height / 2f) + ((height / 2f) * taijutsuScorePercent), 0.0F).color(1f, 1f, 1f, 0.75f).next();
        bufferBuilder.vertex(matrix, (float) x + (width / 2f) + ((width / 2f) * ninjutsuScorePercent), (float) y + height / 2f, 0.0F).color(1f, 1f, 1f, 0.75f).next();
        bufferBuilder.vertex(matrix, (float) x + width / 2f, (float) y + (height / 2f) - ((height / 2f) * staminaScorePercent), 0.0F).color(1f, 1f, 1f, 0.75f).next();
        bufferBuilder.vertex(matrix, (float) x + (width / 2f) - ((width / 2f) * genjutsuScorePercent), (float) y + height / 2f, 0.0F).color(1f, 1f, 1f, 0.75f).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

}
