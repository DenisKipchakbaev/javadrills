package com.teamagile.javadrills;

public class TraceMessage {

    private String text;
    private int severity;

    public TraceMessage(String text, int severity) {
        this.text = text;
        this.severity = severity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}
