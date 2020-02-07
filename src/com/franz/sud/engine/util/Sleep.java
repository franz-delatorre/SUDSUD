package com.franz.sud.engine.util;

public class Sleep {
    public static void sleep(int secs) {
        secs *= 1000;
        try {
            Thread.sleep(secs);
        } catch (InterruptedException e) {

        }
    }
}
