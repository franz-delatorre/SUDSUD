package com.franz.sud.cartridge;

import com.franz.sud.engine.GameEngine;
import com.franz.sud.misc.GameType;

public interface Cartridge {
    void init();
    void start();
    boolean isFinished();
    String getName();
    GameType getGameType();
}
