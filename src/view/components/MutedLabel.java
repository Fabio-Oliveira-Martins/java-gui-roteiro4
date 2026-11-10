package view.components;

import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;

public class MutedLabel extends JLabel implements Themeable {
    public MutedLabel(String text) {
        super(text);
        setFont(new LocalFont("Nunito", FontWeight.REGULAR, 14f).getFont());
        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        setForeground(theme.textMuted);
    }
}