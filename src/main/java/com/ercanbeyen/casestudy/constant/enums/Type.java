package com.ercanbeyen.casestudy.constant.enums;

import java.util.Locale;

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

        name = name.toUpperCase(Locale.ENGLISH);
        return Type.valueOf(name);
    }
}
