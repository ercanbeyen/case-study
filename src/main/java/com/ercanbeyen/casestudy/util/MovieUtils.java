package com.ercanbeyen.casestudy.util;

import com.ercanbeyen.casestudy.exception.EntityNotFound;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MovieUtils {
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

    public static String getMonth(int month) {
        String result;
        switch (month) {
            case 1:
                result = "Jan";
                break;
            case 2:
                result = "Feb";
                break;
            case 3:
                result = "Mar";
                break;
            case 4:
                result = "Apr";
                break;
            case 5:
                result = "May";
                break;
            case 6:
                result = "Jun";
                break;
            case 7:
                result = "Jul";
                break;
            case 8:
                result = "Aug";
                break;
            case 9:
                result = "Sep";
                break;
            case 10:
                result = "Oct";
                break;
            case 11:
                result = "Nov";
                break;
            case 12:
                result = "Dec";
                break;
            default:
                throw new EntityNotFound("Month " + month + " is not found");
        }

        return result;
    }
}
