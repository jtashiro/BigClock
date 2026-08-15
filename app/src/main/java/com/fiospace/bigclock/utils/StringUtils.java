package com.fiospace.bigclock.utils;

public class StringUtils {
    public static String safeStringValueOf(Object obj) {
        return (obj != null) ? String.valueOf(obj) : "";
    }
}
