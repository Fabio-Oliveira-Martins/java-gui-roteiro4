package view.themes;

import java.awt.Component;
import java.awt.Container;

public final class ThemeManager {
    private static Theme current = Theme.LIGHT;
    private static Container root;

    private ThemeManager() {}

    public static Theme current() {
        return current;
    }

    // Define a raiz da árvore que será percorrida (chamado pelo MainFrame)
    public static void install(Container root) {
        ThemeManager.root = root;
    }

    public static void setTheme(Theme theme) {
        if (theme == current) return;
        current = theme;

        if (root != null) {
            apply(root);
            root.revalidate();
            root.repaint();
        }
    }

    private static void apply(Component component) {
        if (component instanceof Themeable themeable) {
            themeable.applyTheme(current);
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                apply(child);
            }
        }
    }
}