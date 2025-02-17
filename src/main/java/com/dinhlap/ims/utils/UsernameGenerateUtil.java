package com.dinhlap.ims.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class UsernameGenerateUtil {
    public static String usernameGenerate(String input, Long id) {
        // Normalize the string to decompose the accented characters
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Use a regex to remove the diacritics (accents)
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String cleanedInput = pattern.matcher(normalized).replaceAll("");

        cleanedInput = cleanedInput.replaceAll("Đ", "D").replaceAll("đ", "d");
        String[] words = cleanedInput.split(" ");

        StringBuilder username = new StringBuilder(words[words.length - 1]);
        for (int i = 0; i < words.length - 1; i++) {
            username.append(words[i].charAt(0));
        }
        username.append(id);

        return username.toString();
    }
}
