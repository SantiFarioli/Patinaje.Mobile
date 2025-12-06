package com.santisoft.patinajemobile.util;

public class DialogEvent {
    public enum Type {
        LOADING,
        SUCCESS,
        ERROR,
        WARNING,
        HIDE_LOADING,
        CONFIRM // Por si necesitamos preguntas Sí/No
    }

    private final Type type;
    private final String title;
    private final String message;

    public DialogEvent(Type type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public Type getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
}