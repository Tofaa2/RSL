package me.tofaa.rsl;

import me.tofaa.rsl.interpreter.runtime.NumberValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Utils {

    private Utils() {
    }


    public static double PI = Math.PI;

    public static <T> String joinToStr(Collection<T> coll, Function<T, String> toStr) {
        return coll.stream().map(toStr).collect(Collectors.joining(", "));
    }

    public static Number operateNumber(String operand, NumberValue l, NumberValue r) {
        var lv = l.value();
        var rv = r.value();
        return switch (operand) {
            case "+" -> {
                switch (lv) {
                    case Long aLong -> {
                        yield lv.longValue() + rv.longValue();
                    }
                    case Integer i -> {
                        yield lv.intValue() + rv.intValue();
                    }
                    case Short i -> {
                        yield lv.shortValue() + rv.shortValue();
                    }
                    case Byte b -> {
                        yield lv.byteValue() + rv.byteValue();
                    }
                    case Float v -> {
                        yield lv.floatValue() + rv.floatValue();
                    }
                    case null, default -> {
                        yield lv.doubleValue() + rv.doubleValue();
                    }
                }
            }
            case "-" -> {
                switch (lv) {
                    case Long aLong -> {
                        yield lv.longValue() - rv.longValue();
                    }
                    case Integer i -> {
                        yield lv.intValue() - rv.intValue();
                    }
                    case Short i -> {
                        yield lv.shortValue() - rv.shortValue();
                    }
                    case Byte b -> {
                        yield lv.byteValue() - rv.byteValue();
                    }
                    case Float v -> {
                        yield lv.floatValue() - rv.floatValue();
                    }
                    case null, default -> {
                        yield lv.doubleValue() - rv.doubleValue();
                    }
                }
            }
            case "*" -> {
                switch (lv) {
                    case Long aLong -> {
                        yield lv.longValue() * rv.longValue();
                    }
                    case Integer i -> {
                        yield lv.intValue() * rv.intValue();
                    }
                    case Short i -> {
                        yield lv.shortValue() * rv.shortValue();
                    }
                    case Byte b -> {
                        yield lv.byteValue() * rv.byteValue();
                    }
                    case Float v -> {
                        yield lv.floatValue() * rv.floatValue();
                    }
                    case null, default -> {
                        yield lv.doubleValue() * rv.doubleValue();
                    }
                }
            }
            case "/" -> {
                switch (lv) {
                    case Long aLong -> {
                        yield lv.longValue() / rv.longValue();
                    }
                    case Integer i -> {
                        yield lv.intValue() / rv.intValue();
                    }
                    case Short i -> {
                        yield lv.shortValue() / rv.shortValue();
                    }
                    case Byte b -> {
                        yield lv.byteValue() / rv.byteValue();
                    }
                    case Float v -> {
                        yield lv.floatValue() / rv.floatValue();
                    }
                    case null, default -> {
                        yield lv.doubleValue() / rv.doubleValue();
                    }
                }
            }
            case "%" -> {
                switch (lv) {
                    case Long aLong -> {
                        yield lv.longValue() % rv.longValue();
                    }
                    case Integer i -> {
                        yield lv.intValue() % rv.intValue();
                    }
                    case Short i -> {
                        yield lv.shortValue() % rv.shortValue();
                    }
                    case Byte b -> {
                        yield lv.byteValue() % rv.byteValue();
                    }
                    case Float v -> {
                        yield lv.floatValue() % rv.floatValue();
                    }
                    case null, default -> {
                        yield lv.doubleValue() % rv.doubleValue();
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + operand);
        };
    }

    public static String readFileContent(Path path) {
        try {
            return Files.readString(path);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map.Entry<K, V>  entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
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
