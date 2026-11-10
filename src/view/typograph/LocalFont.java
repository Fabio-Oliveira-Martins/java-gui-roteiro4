package view.typograph;

import java.awt.*;

public class LocalFont {

    private final Font localFont;

    public LocalFont(String familyName, FontWeight weight, float size) {
        localFont = FontLoader.load(familyName, weight, size);
    }
    public Font getFont() { return localFont; }
}