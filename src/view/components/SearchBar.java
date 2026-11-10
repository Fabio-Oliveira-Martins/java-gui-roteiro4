package view.components;

import utils.IconLoader;
import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class SearchBar extends JPanel implements Themeable {
    private static final String SEARCH_ICON = "search.png";
    private static final String HINT = "Search";
    private static final int RADIUS = 10;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.REGULAR, 12f).getFont();

    private final JTextField field = new JTextField() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) { // placeholder
                FontMetrics fm = getFontMetrics(getFont());
                g.setFont(getFont());
                g.setColor(ThemeManager.current().textMuted);
                g.drawString(HINT, getInsets().left, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            }
        }
    };

    private final JButton button = new JButton(IconLoader.load(SEARCH_ICON, 18)) {
        @Override
        protected void paintComponent(Graphics g) {
            Theme theme = ThemeManager.current();
            Color color = !isEnabled() ? theme.textMuted
                    : getModel().isRollover() ? theme.accentHover : theme.accent;
            RoundedCorners.fillRoundedRectangle(g, color, 0, 0, getWidth(), getHeight(), 0, RADIUS, RADIUS, 0);
            super.paintComponent(g);
        }
    };

    private Color bgColor;
    private boolean actionEnabled = true;
    private Runnable onChange = () -> {};
    private Consumer<String> onSearch = text -> {};

    public SearchBar() {
        setOpaque(false);
        setLayout(new BorderLayout());

        field.setFont(FONT);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 8));

        button.setPreferredSize(new Dimension(48, 40));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(field, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void removeUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void changedUpdate(DocumentEvent e) { onChange.run(); }
        });
        button.addActionListener(e -> search());
        field.addActionListener(e -> search());

        applyTheme(ThemeManager.current());
    }

    private void search() {
        if (actionEnabled) onSearch.accept(getText());
    }

    // Desabilita lupa e Enter durante a busca
    public void setActionEnabled(boolean enabled) {
        actionEnabled = enabled;
        button.setEnabled(enabled);
    }

    public String getText() { return field.getText().trim(); }
    public void setOnChange(Runnable onChange) { this.onChange = onChange; }
    public void setOnSearch(Consumer<String> onSearch) { this.onSearch = onSearch; }

    // escala = largura atual / largura base (1.0 = tamanho original)
    public void scaleTo(double scale) {
        field.setFont(FONT.deriveFont((float) (12 * scale)));
        field.setBorder(BorderFactory.createEmptyBorder(0, (int) (14 * scale), 0, 8));
        button.setPreferredSize(new Dimension((int) (48 * scale), (int) (40 * scale)));
        button.setIcon(IconLoader.load(SEARCH_ICON, (int) (18 * scale)));
    }

    @Override
    public void applyTheme(Theme theme) {
        bgColor = new Color(240,240,240);

        field.setCaretColor(theme.subtitle);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedCorners.fillRoundedRectangle(g, bgColor, 0, 0, getWidth(), getHeight(), RADIUS);
        super.paintComponent(g);
    }
}