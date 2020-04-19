package com.teamagile.javadrills.unittests;

import com.teamagile.javadrills.SlowLogger;

public class FakeSlowLogger extends SlowLogger {

    String written;

    @Override
    public void write(String text) {
        written = text;
    }
}
