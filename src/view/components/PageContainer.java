package view.components;

import view.components.componentsUtils.CustomScrollBarUI;
import view.pages.Page;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;

import javax.swing.*;
import java.awt.*;

public class PageContainer extends JScrollPane implements Themeable {

    public PageContainer() {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        setBorder(null);
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);

        applyTheme(ThemeManager.current());
    }

    public void showPage(Page page) {
        page.render();
        setViewportView(page);
        SwingUtilities.invokeLater(() -> getVerticalScrollBar().setValue(0));
    }

    @Override
    public void applyTheme(Theme theme) {
        setBackground(theme.background);
        getViewport().setBackground(theme.background);

        JScrollBar bar = getVerticalScrollBar();
        bar.setUI(new CustomScrollBarUI(
                withAlpha(theme.text, 70),
                withAlpha(theme.text, 120),
                null,
                12, 6
        ));
        bar.setOpaque(false);
        repaint();
    }

    private static Color withAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
}