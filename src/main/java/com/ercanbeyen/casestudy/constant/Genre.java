package com.ercanbeyen.casestudy.constant;

import com.ercanbeyen.casestudy.util.StringClearUtils;

import java.util.Locale;

public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    FANTASY("Fantasy"),
    DRAMA("Drama"),
    HORROR("Horror"),
    HISTORY("History"),
    SCI_FI("Sci-Fi"),
    THRILLER("Thriller"),
    CRIME("Crime"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy");

    public final String label;

    Genre(String label) {
        this.label = label;
    }

    public static Genre getGenreByUpperCaseName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        } else if (name.equals(SCI_FI.label)) {
            name = "sci_fi";
        }

        name = name.toUpperCase(Locale.ENGLISH);
        //name = StringClearUtils.clearTurkishChars(name.toUpperCase());
        return Genre.valueOf(name);
    }
}
