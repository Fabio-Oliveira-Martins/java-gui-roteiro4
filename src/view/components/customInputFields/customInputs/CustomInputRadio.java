package view.components.customInputFields.customInputs;

import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

import static utils.TextFormatter.capitalize;

public class CustomInputRadio extends JRadioButton implements Themeable {
    private static final Font FONT = new LocalFont("Nunito", FontWeight.REGULAR, 16f).getFont();
    private static final RadioIcon ICON = new RadioIcon(22);

    public CustomInputRadio(String label) {
        super(capitalize(label));

        setFont(FONT);
        setIcon(ICON);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setActionCommand(label.toLowerCase());

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        setForeground(theme.text);
        repaint();
    }
}

class RadioIcon implements Icon {
    private final int size;

    public RadioIcon(int size) {
        this.size = size;
    }

    @Override public int getIconWidth() { return size; }
    @Override public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Theme theme = ThemeManager.current();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(theme.border);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(x + 1, y + 1, size - 3, size - 3);

        if (((AbstractButton) c).getModel().isSelected()) {
            g2.setColor(theme.accent);          // theme color for the selected dot
            g2.fillOval(x + 5, y + 5, size - 10, size - 10);
        }

        g2.dispose();
    }
}