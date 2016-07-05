package ru.prbb.common;

/**
 * @author lesinsa
 */
public final class NullUtils {

    private NullUtils() {
        // nothing to do
    }

    @SafeVarargs
    public static <T> T coalesce(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

    public static String emptyStringAsNull(String s) {
        return s != null && s.trim().length() > 0 ? s : null;
    }
}
