package com.horn.common;

/**
 * @author MaslovDV
 */
public final class NumberUtils {

    private NumberUtils() {
        // nothing to do
    }

    @SuppressWarnings("unchecked")
    public static <T> T max(Comparable<T>... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("At least one parameter should be provided");
        }
        Comparable<T> t = values[0];
        for (int i = 1; i < values.length; i++) {
            Comparable<T> value = values[i];
            if (t.compareTo((T) value) < 0) {
                t = value;
            }
        }
        return (T) t;
    }

    public static <T extends Comparable<T>> boolean greater(T number1, T number2) {
        return number1.compareTo(number2) > 0;
    }

    public static <T extends Comparable<T>> boolean less(T number1, T number2) {
        return number1.compareTo(number2) < 0;
    }
}
