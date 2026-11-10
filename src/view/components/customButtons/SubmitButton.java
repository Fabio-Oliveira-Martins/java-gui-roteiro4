package view.components.customButtons;

import utils.IconLoader;
import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class SubmitButton extends JButton implements Themeable {
    private static final int PADDING = 8;
    private static final int RADIUS = 16;
    private static final int ICON_SIZE = 16;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.BOLD, 16f).getFont();

    private Theme theme = ThemeManager.current();

    public SubmitButton(String txt) {
        this(txt, null);
    }

    // iconName: apenas o nome do arquivo (ex.: "PLACEHOLDER_confirm.png") ou null
    public SubmitButton(String txt, String iconName) {
        super(txt);
        RoundedCorners.apply(this, RADIUS, null, 0, PADDING);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(FONT);

        setIcon(IconLoader.load(iconName, ICON_SIZE));
        setIconTextGap(8);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        this.theme = theme;
        setForeground(theme.accentText);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color color = getModel().isRollover() ? theme.accentHover : theme.accent;
        RoundedCorners.fillRoundedRectangle(g, color, 0, 0, getWidth(), getHeight(), RADIUS);
        super.paintComponent(g);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}