package com.dinhlap.ims.utils;

public class PasswordGenerateUtil {
    public static String passwordGenerate() {
        final String ALPHA_UPPER_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String ALPHA_LOWER_STRING = "abcdefghijklmnopqrstuvwxyz";
        final String NUMERIC_STRING = "0123456789";
        final String SPECIAL_STRING = "!@#$%^&*()_+";

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            password.append(ALPHA_UPPER_STRING.charAt((int) (Math.random() * ALPHA_UPPER_STRING.length())));
            password.append(ALPHA_LOWER_STRING.charAt((int) (Math.random() * ALPHA_LOWER_STRING.length())));
            password.append(NUMERIC_STRING.charAt((int) (Math.random() * NUMERIC_STRING.length())));
            password.append(SPECIAL_STRING.charAt((int) (Math.random() * SPECIAL_STRING.length())));
        }

        return password.toString();
    }
}
