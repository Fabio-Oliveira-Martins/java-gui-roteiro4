package view.components;

import view.components.componentsUtils.RoundedCorners;
import view.pages.Page;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NavigationBar extends JPanel implements Themeable {
    private static final String ICONS_PATH = "/icons/";

    private final ButtonGroup group = new ButtonGroup();
    private final List<NavButton> buttons = new ArrayList<>();
    private Consumer<Page> onNavigate = page -> {};

    public NavigationBar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 14));
        setPreferredSize(new Dimension(180, 0));
        applyTheme(ThemeManager.current());
    }

    public void setOnNavigate(Consumer<Page> onNavigate) {
        this.onNavigate = onNavigate;
    }

    public void addItem(String label, String iconName, String lightIconName, Supplier<Page> pageFactory) {
        NavButton button = new NavButton(
                label,
                ICONS_PATH + iconName,
                ICONS_PATH + lightIconName
        );
        button.addActionListener(e -> onNavigate.accept(pageFactory.get()));

        group.add(button);
        buttons.add(button);

        add(button);
        add(Box.createVerticalStrut(6));
    }

    public void addSeparator() {
        add(Box.createVerticalStrut(10));
        add(new ThemedSeparator());
        add(Box.createVerticalStrut(16));
    }

    public void selectFirst() {
        if (!buttons.isEmpty()) {
            buttons.get(0).doClick();
        }
    }

    @Override
    public void applyTheme(Theme theme) {
        setBackground(theme.sidebar);
    }

    // ===== Separador =====
    private static class ThemedSeparator extends JSeparator implements Themeable {
        ThemedSeparator() {
            applyTheme(ThemeManager.current());
        }

        @Override
        public void applyTheme(Theme theme) {
            setForeground(theme.border);
        }
    }

    // ===== Botão =====
    private static class NavButton extends JToggleButton implements Themeable {
        private static final Font FONT = new LocalFont("Nunito", FontWeight.REGULAR, 14f).getFont();
        private static final int ICON_SIZE = 20;

        private final ImageIcon icon;       // versão normal (escura)
        private final ImageIcon lightIcon;  // versão clara

        NavButton(String text, String iconPath, String lightIconPath) {
            super(text);
            this.icon = loadIcon(iconPath);
            this.lightIcon = loadIcon(lightIconPath);

            setFont(FONT);
            setHorizontalAlignment(SwingConstants.LEFT);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setIconTextGap(12);
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            addItemListener(e -> applyTheme(ThemeManager.current()));
            applyTheme(ThemeManager.current());
        }

        // Retorna null se o arquivo não existir: o item fica só com o texto
        private static ImageIcon loadIcon(String path) {
            var url = NavButton.class.getResource(path);
            if (url == null) {
                System.err.println("Icon not found: " + path);
                return null;
            }

            Image scaled = new ImageIcon(url).getImage()
                    .getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }

        @Override
        public void applyTheme(Theme theme) {
            // Fundo roxo (selecionado) ou tema escuro: ícone claro
            boolean useLight = isSelected() || theme == Theme.DARK;

            ImageIcon chosen = useLight ? lightIcon : icon;
            setIcon(chosen != null ? chosen : (useLight ? icon : lightIcon)); // fallback se faltar uma versão

            setForeground(isSelected() ? theme.accentText : theme.text);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Theme theme = ThemeManager.current();
            Color bg = isSelected() ? theme.accent : getModel().isRollover() ? theme.hover : null;
            RoundedCorners.fillRoundedRectangle(g, bg, 0, 0, getWidth(), getHeight(), 999);
            super.paintComponent(g);
        }
    }
    public void addAction(
            String label,
            String iconName,
            String lightIconName,
            Runnable action
    ) {
        NavButton button = new NavButton(
                label,
                ICONS_PATH + iconName,
                ICONS_PATH + lightIconName
        );

        button.addActionListener(e -> {
            button.setSelected(false);

            group.clearSelection();

            action.run();
        });

        add(button);
        add(Box.createVerticalStrut(6));
    }
}
