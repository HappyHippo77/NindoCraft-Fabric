package io.github.happyhippo77.nindocraft.client;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.happyhippo77.nindocraft.NindoCraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class NindoCraftIdGui extends LightweightGuiDescription implements PropertyDelegateHolder {
    private int exp;
    private int nextLevel;

    public static final int guiWidth = 256;
    public static final int guiHeight = 240;
    public static final int stats_chart_size = 100;
    public final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public NindoCraftIdGui(int exp, int level, int nextLevel, int statPoints, int staminaScore, int ninjutsuScore, int genjutsuScore, int taijutsuScore) {
        this.exp = exp;
        this.nextLevel = nextLevel;

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(guiWidth, guiHeight);
        root.setInsets(Insets.ROOT_PANEL);

        Text titleText = Text.translatable("id.nindocraft.title");
        WLabel title = new WLabel(titleText, 0xFFFFFF);
        root.add(title, (guiWidth / 2) - (textRenderer.getWidth(titleText) / 2), 0, textRenderer.getWidth(titleText), textRenderer.fontHeight);

        WBar nextLevelBar = new WBar(new Identifier(NindoCraft.MOD_ID, "textures/gui/id_level_progress_background.png"),
                new Identifier(NindoCraft.MOD_ID, "textures/gui/id_level_progress.png"), 0, 1, WBar.Direction.RIGHT).withTooltip(Text.translatable("id.nindocraft.exp_needed", exp, nextLevel));
        int textureWidth = 136;
        int textureHeight = 5;
        root.add(nextLevelBar, (guiWidth / 2) - (textureWidth / 2), textRenderer.fontHeight + 5, textureWidth, textureHeight);

        Text currentLevelText = Text.translatable("id.nindocraft.level", level);
        WLabel currentLevelLabel = new WLabel(currentLevelText, 0xFFFFFF);
        root.add(currentLevelLabel, (guiWidth / 2) - (textureWidth / 2) - textRenderer.getWidth(currentLevelText) - 5, textRenderer.fontHeight + 5 - (textureHeight / 2), textRenderer.getWidth(currentLevelText), textRenderer.fontHeight);

        Text nextLevelText = Text.translatable("id.nindocraft.level", level + 1);
        WLabel nextLevelLabel = new WLabel(nextLevelText, 0xFFFFFF);
        root.add(nextLevelLabel, (guiWidth / 2) + (textureWidth / 2) + 5, textRenderer.fontHeight + 5 - (textureHeight / 2), textRenderer.getWidth(nextLevelText), textRenderer.fontHeight);

        Text statPointsText = Text.translatable("id.nindocraft.stat_points", statPoints);
        WLabel statPointsLabel = new WLabel(statPointsText, 0xFFFFFF);
        root.add(statPointsLabel, (guiWidth / 2) - (textRenderer.getWidth(statPointsText) / 2), guiHeight - textRenderer.fontHeight - 5, textRenderer.getWidth(statPointsText), textRenderer.fontHeight);

        NindoCraftRadarChart chart = new NindoCraftRadarChart(staminaScore, ninjutsuScore, taijutsuScore, genjutsuScore );
        root.add(chart, (guiWidth / 2) - (stats_chart_size / 2), (guiHeight / 2) - (stats_chart_size / 2), stats_chart_size, stats_chart_size);

        Text addStaminaButtonText = Text.literal("(+)").formatted(Formatting.YELLOW);
        WStatButton addStaminaButton = new WStatButton(addStaminaButtonText);
        addStaminaButton.setOnClick(new AddStatButtonRunnable(0));
        root.add(addStaminaButton, (guiWidth / 2) - textRenderer.getWidth(addStaminaButtonText) / 2, (guiHeight / 2) - (stats_chart_size / 2) - textRenderer.fontHeight, textRenderer.getWidth(addStaminaButtonText), textRenderer.fontHeight);
        Text addStaminaLabelText = Text.literal("Sta. (" + staminaScore + ")").formatted(Formatting.WHITE);
        WLabel addStaminaLabel = new WLabel(addStaminaLabelText);
        root.add(addStaminaLabel, (guiWidth / 2) - textRenderer.getWidth(addStaminaLabelText) / 2, (guiHeight / 2) - (stats_chart_size / 2) - (textRenderer.fontHeight * 2), textRenderer.getWidth(addStaminaLabelText), textRenderer.fontHeight);

        Text addNinjutsuButtonText = Text.literal("(+)").formatted(Formatting.YELLOW);
        WStatButton addNinjutsuButton = new WStatButton(addNinjutsuButtonText);
        addNinjutsuButton.setOnClick(new AddStatButtonRunnable(1));
        root.add(addNinjutsuButton, (guiWidth / 2) + (stats_chart_size / 2) + 3, (guiHeight / 2) - (textRenderer.fontHeight / 2), textRenderer.getWidth(addNinjutsuButtonText), textRenderer.fontHeight);
        Text addNinjutsuLabelText = Text.literal("Nin. (" + ninjutsuScore + ")").formatted(Formatting.WHITE);
        WLabel addNinjutsuLabel = new WLabel(addNinjutsuLabelText);
        root.add(addNinjutsuLabel, (guiWidth / 2) + (stats_chart_size / 2) + textRenderer.getWidth(addNinjutsuButtonText) + 6, (guiHeight / 2) - (textRenderer.fontHeight / 2), textRenderer.getWidth(addNinjutsuLabelText), textRenderer.fontHeight);

        Text addTaijutsuButtonText = Text.literal("(+)").formatted(Formatting.YELLOW);
        WStatButton addTaijutsuButton = new WStatButton(addTaijutsuButtonText);
        addTaijutsuButton.setOnClick(new AddStatButtonRunnable(2));
        root.add(addTaijutsuButton, (guiWidth / 2) - textRenderer.getWidth(addTaijutsuButtonText) / 2, (guiHeight / 2) + (stats_chart_size / 2) + 2, textRenderer.getWidth(addTaijutsuButtonText), textRenderer.fontHeight);
        Text addTaijutsuLabelText = Text.literal("Tai. (" + taijutsuScore + ")").formatted(Formatting.WHITE);
        WLabel addTaijutsuLabel = new WLabel(addTaijutsuLabelText);
        root.add(addTaijutsuLabel, (guiWidth / 2) - textRenderer.getWidth(addTaijutsuLabelText) / 2, (guiHeight / 2) + (stats_chart_size / 2) + textRenderer.fontHeight + 2, textRenderer.getWidth(addTaijutsuLabelText), textRenderer.fontHeight);

        Text addGenjutsuButtonText = Text.literal("(+)").formatted(Formatting.YELLOW);
        WStatButton addGenjutsuButton = new WStatButton(addGenjutsuButtonText);
        addGenjutsuButton.setOnClick(new AddStatButtonRunnable(3));
        root.add(addGenjutsuButton, (guiWidth / 2) - (stats_chart_size / 2) - textRenderer.getWidth(addGenjutsuButtonText) - 3, (guiHeight / 2) - (textRenderer.fontHeight / 2), textRenderer.getWidth(addGenjutsuButtonText), textRenderer.fontHeight);
        Text addGenjutsuLabelText = Text.literal("Gen. (" + genjutsuScore + ")").formatted(Formatting.WHITE);
        WLabel addGenjutsuLabel = new WLabel(addGenjutsuLabelText);
        root.add(addGenjutsuLabel, (guiWidth / 2) - (stats_chart_size / 2) - textRenderer.getWidth(addGenjutsuButtonText) - 6 - textRenderer.getWidth(addGenjutsuLabelText), (guiHeight / 2) - (textRenderer.fontHeight / 2), textRenderer.getWidth(addGenjutsuLabelText), textRenderer.fontHeight);


//        WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
//        root.add(icon, 0, 2, 1, 1);
//
//        WButton button = new WButton(Text.translatable("gui.nindocraft.examplebutton"));
//        root.add(button, 0, 3, 4, 1);
//
//        WLabel label = new WLabel(Text.literal("Test"), 0xFFFFFF);
//        root.add(label, 0, 4, 2, 1);

        root.validate(this);
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return exp;
                    case 1:
                        return nextLevel;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        exp = value;
                        break;
                    case 1:
                        nextLevel = value;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }
}
