package com.ercanbeyen.casestudy.util;

public class StringClearUtils {
    public static String clearTurkishChars(String str) {
        String result = str;

        char[] turkishChars = new char[] {0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
        char[] englishChars = new char[] {'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};

        for (int i = 0; i < turkishChars.length; i++) {
            result = result.replaceAll(String.valueOf(turkishChars[i]), String.valueOf(englishChars[i]));
        }
        return result;
    }
}
