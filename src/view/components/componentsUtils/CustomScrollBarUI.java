package view.components.componentsUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollBarUI extends BasicScrollBarUI {
    private final Color thumbColor;
    private final Color hoverColor;
    private final Color trackColor;
    private final int thickness;
    private final int radius;

    public CustomScrollBarUI(
            Color thumbColor,
            Color hoverColor,
            Color trackColor,
            int thickness,
            int radius
    ) {
        this.thumbColor = thumbColor;
        this.hoverColor = hoverColor;
        this.trackColor = trackColor;
        this.thickness = Math.max(1, thickness);
        this.radius = Math.max(0, radius);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createEmptyButton();
    }

    private JButton createEmptyButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    @Override
    public Dimension getPreferredSize(JComponent component) {
        Dimension size = super.getPreferredSize(component);

        if (component instanceof JScrollBar bar) {
            if (bar.getOrientation() == JScrollBar.VERTICAL) {
                size.width = thickness;
            } else {
                size.height = thickness;
            }
        }

        return size;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        if (trackColor != null) {
            g.setColor(trackColor);
            g.fillRect(
                    trackBounds.x,
                    trackBounds.y,
                    trackBounds.width,
                    trackBounds.height
            );
        }
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Color color = isThumbRollover()
                ? hoverColor
                : thumbColor;

        int inset = 2;

        RoundedCorners.fillRoundedRectangle(
                g,
                color,
                thumbBounds.x + inset,
                thumbBounds.y + inset,
                thumbBounds.width - inset * 2,
                thumbBounds.height - inset * 2,
                radius
        );
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return getScrollbarOrientationSize(30);
    }

    private Dimension getScrollbarOrientationSize(int length) {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(thickness, length);
        }

        return new Dimension(length, thickness);
    }
}
