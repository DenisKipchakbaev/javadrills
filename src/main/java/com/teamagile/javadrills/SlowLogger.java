package com.teamagile.javadrills;

public class SlowLogger implements Logger {
    @Override
    public void write(String text) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
