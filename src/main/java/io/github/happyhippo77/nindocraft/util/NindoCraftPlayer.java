package io.github.happyhippo77.nindocraft.util;

import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;

public interface NindoCraftPlayer {
    boolean isHandSigning();
    void setHandSigning(boolean bool);
    HandSignSequence getHandSignSequence();
    void setHandSignSequence(HandSignSequence sequence);

    int getChakra();
    void setChakra(int i);

    int getStamina();
    void setStamina(int i);

    int getStaminaScore();
    void setStaminaScore(int i);

    int getMaxStamina();

    boolean doRechargeStamina();
    void setRechargeStamina(boolean bool);
}
