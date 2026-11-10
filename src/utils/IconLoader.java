package utils;

import javax.swing.ImageIcon;
import java.awt.Image;

public final class IconLoader {
    private static final String ICONS_PATH = "/icons/";

    private IconLoader() {}

    public static ImageIcon load(String name, int size) {
        if (name == null) return null;

        var url = IconLoader.class.getResource(ICONS_PATH + name);
        if (url == null) {
            System.err.println("Icon not found: " + ICONS_PATH + name);
            return null;
        }

        Image scaled = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}