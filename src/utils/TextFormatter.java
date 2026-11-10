package utils;

import java.util.regex.Pattern;

public class TextFormatter {

    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-zA-Z]");

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        char[] chars = string.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0 || !LOWER_CASE_PATTERN.matcher(String.valueOf(chars[i - 1])).matches()) {
                chars[i] = Character.toUpperCase(chars[i]);
            }
        }

        return String.valueOf(chars);
    }
}