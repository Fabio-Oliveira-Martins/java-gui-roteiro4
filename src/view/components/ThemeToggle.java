package view.components;

import view.themes.Theme;
import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class ThemeToggle extends JCheckBox {

    public ThemeToggle() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setSelected(ThemeManager.current() == Theme.DARK); // estado inicial
        addItemListener(e ->
                ThemeManager.setTheme(isSelected() ? Theme.DARK : Theme.LIGHT)
        );
    }

    // Os ícones são criados por você: sol (tema claro) e lua (tema escuro)
    public void setIcons(Icon lightThemeIcon, Icon darkThemeIcon) {
        setIcon(lightThemeIcon);
        setSelectedIcon(darkThemeIcon);
    }
}