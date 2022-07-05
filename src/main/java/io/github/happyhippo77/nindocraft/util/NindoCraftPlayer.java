package io.github.happyhippo77.nindocraft.util;

import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import java.util.ArrayList;

public interface NindoCraftPlayer {
    boolean isHandSigning();
    void setHandSigning(boolean bool);
    HandSignSequence getHandSignSequence();
    void setHandSignSequence(HandSignSequence sequence);
    ArrayList<String> getKnownJutsu();
    void setKnownJutsu(ArrayList<String> arrayList);

    int getChakra();
    void setChakra(int i);

    int getStamina();
    void setStamina(int i);

    int getMaxStamina();

    int getExp();
    void addExp(int i);
    int getLevel();
    void checkExp();
    int getNextLevel();
    int getStatPoints();
    void setStatPoints(int i);
    int getStaminaScore();
    void setStaminaScore(int i);
    int getNinjutsuScore();
    void setNinjutsuScore(int i);
    int getGenjutsuScore();
    void setGenjutsuScore(int i);
    int getTaijutsuScore();
    void setTaijutsuScore(int i);

    boolean canRechargeStamina();
    void setRechargeStamina(boolean bool);
}
