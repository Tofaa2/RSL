package me.tofaa.rsl;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {

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
