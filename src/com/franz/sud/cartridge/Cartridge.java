package com.franz.sud.cartridge;

public interface Cartridge {
    void init();
    void start();
    boolean isFinished();
    String getName();
}
