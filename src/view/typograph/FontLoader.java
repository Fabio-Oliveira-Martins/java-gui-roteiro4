package view.typograph;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class FontLoader {

    private FontLoader() {}

    public static Font load( String family, FontWeight weight, float size) {

        String fileName = family + "-" + switch (weight) {
            case REGULAR -> "Regular";
            case MEDIUM -> "Medium";
            case SEMIBOLD -> "SemiBold";
            case BOLD -> "Bold";
            case EXTRABOLD -> "ExtraBold";
        } + ".ttf";

        String path = "/fonts/static/" + fileName;

        try (InputStream stream = FontLoader.class.getResourceAsStream(path)) {

            if (stream == null) {
                throw new IllegalArgumentException("Font not found on classpath: " + path);
            }

            return Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);

            } catch (IOException | FontFormatException e) {
                throw new RuntimeException("Could not load font: " + path, e);
        }
    }
}