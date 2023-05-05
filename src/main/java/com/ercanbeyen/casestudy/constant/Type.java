package com.ercanbeyen.casestudy.constant;

import com.ercanbeyen.casestudy.util.StringClearUtils;

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

        //name = StringClearUtils.clearTurkishChars(name.toUpperCase());
        //name = name.toUpperCase();
        name = name.toUpperCase(Locale.ENGLISH);
        return Type.valueOf(name);
    }
}
