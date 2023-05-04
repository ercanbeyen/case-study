package com.ercanbeyen.casestudy.constant;

public enum Type {
    MOVIE("movie"),
    SERIES("series");

    public final String label;

    Type(String label) {
        this.label = label;
    }

    public static Type getTypeByUpperCaseName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return Type.valueOf(name.toUpperCase());
    }
}
