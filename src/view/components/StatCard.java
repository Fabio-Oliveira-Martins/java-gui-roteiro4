package view.components;

import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel implements Themeable {
    private static final Font VALUE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 16f).getFont();
    private static final Font LABEL_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 11f).getFont();

    private final Color tint;
    private final JLabel valueLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();

    public StatCard(String iconName, Color color, int value, String label) {
        tint = new Color(color.getRed(), color.getGreen(), color.getBlue(), 35);

        setOpaque(false);
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        valueLabel.setText(String.valueOf(value));
        valueLabel.setFont(VALUE_FONT);
        nameLabel.setText(label);
        nameLabel.setFont(LABEL_FONT);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(Box.createVerticalGlue());
        text.add(valueLabel);
        text.add(nameLabel);
        text.add(Box.createVerticalGlue());

        JPanel badge = new JPanel(new GridBagLayout()); // centraliza sem esticar o círculo
        badge.setOpaque(false);
        badge.add(IconBadge.circle(color, iconName, 36));

        add(badge, BorderLayout.WEST);
        add(text, BorderLayout.CENTER);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        valueLabel.setForeground(theme.text);
        nameLabel.setForeground(theme.textMuted);
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedCorners.fillRoundedRectangle(g, tint, 0, 0, getWidth(), getHeight(), 12);
        super.paintComponent(g);
    }
}