package view.components.sections;

import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class Section extends JPanel implements Themeable {
    private static final int RADIUS = 16;
    private static final int PADDING = 16;
    private static final Font TITLE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 24f).getFont();


    private final JLabel titleLabel;
    private JComponent content;
    private Color bgColor;

    public Section(String title) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 12));
        RoundedCorners.apply(this, RADIUS, null, 0, PADDING);

        titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);

        applyTheme(ThemeManager.current());
    }

    // Troca o conteúdo da seção (o anterior é removido para não deixar resíduo)
    public void setContent(JComponent content) {
        if (this.content != null) {
            remove(this.content);
        }
        this.content = content;
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setTitleAlignment(int alignment) {
        titleLabel.setHorizontalAlignment(alignment);
    }

    @Override
    public void applyTheme(Theme theme) {
        bgColor = theme.surface;

        titleLabel.setForeground(theme.text);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        RoundedCorners.fillRoundedRectangle(g, bgColor, 0, 0, getWidth(), getHeight(), RADIUS);
    }
}