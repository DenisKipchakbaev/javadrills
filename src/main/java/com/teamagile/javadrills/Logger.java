package com.teamagile.javadrills;

public interface Logger {
    void write(String text);

    void write(TraceMessage traceMessage);
}
