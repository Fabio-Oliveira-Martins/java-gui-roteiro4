package view.components;

import model.Category;
import utils.IconLoader;
import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class CategoryCard extends JPanel implements Themeable {
    private static final Font NAME_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 13f).getFont();
    private static final Font COUNT_FONT = new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();

    private final JLabel nameLabel = new JLabel();
    private final JLabel countLabel = new JLabel();
    private Color bgColor;

    public CategoryCard(Category category, int count) {
        setOpaque(false);
        setLayout(new BorderLayout(10, 0));

        nameLabel.setText(category.getDisplayName());
        nameLabel.setFont(NAME_FONT);
        countLabel.setText(String.valueOf(count));
        countLabel.setFont(COUNT_FONT);

        JPanel text = new JPanel(new GridBagLayout()); // centraliza na vertical
        text.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 8);
        text.add(nameLabel, gbc);
        gbc.weightx = 1.0;
        text.add(countLabel, gbc);

        add(IconBadge.leftRounded(CategoryStyle.color(category), CategoryStyle.icon(category), 40, 40),
                BorderLayout.WEST);
        add(text, BorderLayout.CENTER);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        bgColor = new Color(240,240,240);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedCorners.fillRoundedRectangle(g, bgColor, 0, 0, getWidth(), getHeight(), 8);
        super.paintComponent(g);
    }
}

class IconBadge extends JLabel {
    private final Color color;
    private final int topLeft, topRight, bottomRight, bottomLeft;

    public static IconBadge circle(Color color, String iconName, int size) {
        return new IconBadge(color, iconName, size, size, 999, 999, 999, 999);
    }

    public static IconBadge leftRounded(Color color, String iconName, int width, int height) {
        return new IconBadge(color, iconName, width, height, 8, 0, 0, 8);
    }

    private IconBadge(Color color, String iconName, int width, int height, int tl, int tr, int br, int bl) {
        this.color = color;
        this.topLeft = tl;
        this.topRight = tr;
        this.bottomRight = br;
        this.bottomLeft = bl;

        setIcon(IconLoader.load(iconName, 18));
        setHorizontalAlignment(CENTER);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedCorners.fillRoundedRectangle(g, color, 0, 0, getWidth(), getHeight(),
                topLeft, topRight, bottomRight, bottomLeft);
        super.paintComponent(g);
    }
}