package com.ercanbeyen.casestudy.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WatchingItemUtils {
    private static final String N_A = "N/A";
    public static Integer handleInteger(String label) {
        return label.equals(N_A) ? null : Integer.parseInt(label);
    }

    public static Double handleDouble(String label) {
        return label.equals(N_A) ? null : Double.parseDouble(label);
    }

    public static LocalDate handleLocalDate(String label) {
        return label.equals(N_A) ? null :  LocalDate.parse(label, DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));
    }
}
