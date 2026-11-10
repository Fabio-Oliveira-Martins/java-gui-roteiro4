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

public class SecondaryButton extends JButton implements Themeable {
    private static final int RADIUS = 16;
    private static final int ICON_SIZE = 16;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();

    private final boolean outlined;
    private Theme theme = ThemeManager.current();

    private static final Color DANGER = new Color(255, 71, 87);
    private final boolean danger;

    // Contorno roxo, fundo transparente
    public static SecondaryButton outlined(String txt) { return new SecondaryButton(txt, null, true, false); }
    public static SecondaryButton filled(String txt, String iconName) { return new SecondaryButton(txt, iconName, false, false); }
    public static SecondaryButton danger(String txt) { return new SecondaryButton(txt, null, true, true); }


    private SecondaryButton(String txt, String iconName, boolean outlined, boolean danger) {
        super(txt);
        this.outlined = outlined;
        this.danger = danger;

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
        Color main = danger ? DANGER : theme.accent;
        setForeground(outlined ? main : theme.text);
        RoundedCorners.apply(this, RADIUS, outlined ? main : null, outlined ? 2 : 0, 8, 16, 8, 16);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color fill = getModel().isRollover() ? theme.hover : outlined ? null : theme.inputBackground;
        RoundedCorners.fillRoundedRectangle(g, fill, 0, 0, getWidth(), getHeight(), RADIUS);
        super.paintComponent(g);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}