package io.lihongbin.utils;

public class UUID {

    public static String get() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

}
