package io.github.happyhippo77.nindocraft.client;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.Text;

public class WStatButton extends WLabel {
    AddStatButtonRunnable runnable;

    public WStatButton(Text text) {
        super(text);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        runnable.run();
        return super.onClick(x, y, button);
    }

    public void setOnClick(AddStatButtonRunnable r) {
        runnable = r;
    }
}
