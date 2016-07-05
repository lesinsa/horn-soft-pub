package ru.prbb.common;

import java.util.Collection;

/**
 * @author lesinsa
 */
public final class StringUtils {

    private StringUtils() {
        // nothing to do
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String secureString(String s, int store) {
        return secureString(s, store, '*');
    }

    public static String secureString(String s, int store, char secureChar) {
        int secureLength = s.length() - store * 2;
        if (secureLength > 0) {
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i >= store && i < s.length() - store) {
                    chars[i] = secureChar;
                } else {
                    chars[i] = s.charAt(i);
                }
            }
            return String.valueOf(chars);
        } else {
            return s;
        }
    }

    public static String secureStringWithReplace(String s, int store, String replaceString) {
        int secureLength = s.length() - store * 2;
        if (secureLength > 0) {
            return s.substring(0, store) + replaceString + s.substring(s.length() - store, s.length());
        } else {
            return s;
        }
    }

    public static <T> String join(Collection<T> listStrings, CharSequence delimiter) {
        if (delimiter == null) {
            throw new IllegalArgumentException("delimiter can not be null");
        }
        CharSequence d = "";
        StringBuilder sb = new StringBuilder("");
        for (T s : listStrings) {
            sb.append(d)
                    .append(s.toString());
            d = delimiter;
        }
        return sb.toString();
    }

    public static String lpad(String str, char c, int length) {
        if (str == null) {
            return null;
        }
        int toAddCount = length - str.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < toAddCount; i++) {
            sb.append(c);
        }
        sb.append(str);
        return sb.toString();
    }

    public static String safeToString(Integer value) {
        return value != null ? value.toString() : null;
    }
}
