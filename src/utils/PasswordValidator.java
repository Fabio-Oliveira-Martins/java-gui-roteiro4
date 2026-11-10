package utils;

import java.util.regex.Pattern;

public final class PasswordValidator {
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 16;
    public static final String RULES = "The password must have " + MIN_LENGTH + " to " + MAX_LENGTH
                    + " characters, including uppercase, lowercase, number and special character.";

    // lookaheads: pelo menos 1 minúscula, 1 maiúscula, 1 dígito e 1 caractere especial
    private static final Pattern PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{" + MIN_LENGTH + "," + MAX_LENGTH + "}$");

    private PasswordValidator() {}

    public static boolean isValid(String password) {
        return password != null && PATTERN.matcher(password).matches();
    }
}