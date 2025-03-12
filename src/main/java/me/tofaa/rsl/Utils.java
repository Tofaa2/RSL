package me.tofaa.rsl;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {

    }

    public static boolean isNumeric(final String cs) {
        if (cs == null || cs.isBlank()) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean isAlpha(final String cs) {
        if (cs == null || cs.isBlank()) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    // TODO: At some point ad f d l identifiers.
    // For 1.0f 1.0d 1.0 <- auto double and 1l 1 <- auto int
    public static Number numFromString(String s) {
        if (s.contains(".")) {
            return Double.parseDouble(s);
        }
        else {
            return Integer.parseInt(s);
        }
    }

    public static List<String> stringListFromCharArr(char[] array) {
        List<String> s =new ArrayList<>();
        for (var a : array) {
            s.add(String.valueOf(a));
        }
        return s;
    }

}
