package view.components.componentsUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Path2D;

public final class RoundedCorners {

    private RoundedCorners() {
    }

    // ==========================================
    // APPLY METHODS
    // ==========================================

    // Uniform radius, default padding (2)
    public static void apply(
            JComponent component,
            int radius,
            Color borderColor,
            int thickness) {

        apply(component, radius, borderColor, thickness, 2);
    }

    // Uniform radius and padding
    public static void apply(
            JComponent component,
            int radius,
            Color borderColor,
            int thickness,
            int padding) {

        apply(
                component,
                radius,
                radius,
                radius,
                radius,
                borderColor,
                thickness,
                padding
        );
    }

    // Uniform radius and individual padding
    public static void apply(
            JComponent component,
            int radius,
            Color borderColor,
            int thickness,
            int paddingTop,
            int paddingRight,
            int paddingBottom,
            int paddingLeft) {

        apply(
                component,
                radius,
                radius,
                radius,
                radius,
                borderColor,
                thickness,
                paddingTop,
                paddingRight,
                paddingBottom,
                paddingLeft
        );
    }

    // Individual radius and uniform padding
    public static void apply(
            JComponent component,
            int topLeftRadius,
            int topRightRadius,
            int bottomRightRadius,
            int bottomLeftRadius,
            Color borderColor,
            int thickness,
            int padding) {

        apply(
                component,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
                borderColor,
                thickness,
                padding,
                padding,
                padding,
                padding
        );
    }

    // Individual radius and individual padding
    public static void apply(
            JComponent component,
            int topLeftRadius,
            int topRightRadius,
            int bottomRightRadius,
            int bottomLeftRadius,
            Color borderColor,
            int thickness,
            int paddingTop,
            int paddingRight,
            int paddingBottom,
            int paddingLeft) {

        component.setBorder(
                new RoundedBorder(
                        topLeftRadius,
                        topRightRadius,
                        bottomRightRadius,
                        bottomLeftRadius,
                        borderColor,
                        thickness,
                        paddingTop,
                        paddingRight,
                        paddingBottom,
                        paddingLeft
                )
        );
    }

    // ==========================================
    // FILL ROUNDED RECTANGLE
    // ==========================================

    // Uniform radius
    public static void fillRoundedRectangle(
            Graphics g,
            Color color,
            int x,
            int y,
            int width,
            int height,
            int radius) {

        fillRoundedRectangle(
                g,
                color,
                x,
                y,
                width,
                height,
                radius,
                radius,
                radius,
                radius
        );
    }

    // Individual radius
    public static void fillRoundedRectangle(
            Graphics g,
            Color color,
            int x,
            int y,
            int width,
            int height,
            int topLeftRadius,
            int topRightRadius,
            int bottomRightRadius,
            int bottomLeftRadius) {

        if (color == null || width <= 0 || height <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        Path2D path = createRoundedPath(
                x,
                y,
                width - 1,
                height - 1,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
                0
        );

        g2.setColor(color);
        g2.fill(path);

        g2.dispose();
    }

    // ==========================================
    // CREATE ROUNDED PATH
    // ==========================================

    private static Path2D createRoundedPath(
            float x,
            float y,
            float width,
            float height,
            int topLeftRadius,
            int topRightRadius,
            int bottomRightRadius,
            int bottomLeftRadius,
            float offset) {

        float left = x + offset;
        float top = y + offset;
        float right = x + width - offset;
        float bottom = y + height - offset;

        float maxRadius = Math.max(
                0,
                Math.min(
                        (right - left) / 2f,
                        (bottom - top) / 2f
                )
        );

        float tl = Math.min(Math.max(0, topLeftRadius), maxRadius);
        float tr = Math.min(Math.max(0, topRightRadius), maxRadius);
        float br = Math.min(Math.max(0, bottomRightRadius), maxRadius);
        float bl = Math.min(Math.max(0, bottomLeftRadius), maxRadius);

        Path2D path = new Path2D.Float();

        path.moveTo(left + tl, top);

        path.lineTo(right - tr, top);
        path.quadTo(right, top, right, top + tr);

        path.lineTo(right, bottom - br);
        path.quadTo(right, bottom, right - br, bottom);

        path.lineTo(left + bl, bottom);
        path.quadTo(left, bottom, left, bottom - bl);

        path.lineTo(left, top + tl);
        path.quadTo(left, top, left + tl, top);

        path.closePath();

        return path;
    }

    // ==========================================
    // ROUNDED BORDER
    // ==========================================

    private static class RoundedBorder extends AbstractBorder {

        private final int topLeftRadius;
        private final int topRightRadius;
        private final int bottomRightRadius;
        private final int bottomLeftRadius;

        private final Color borderColor;
        private final int thickness;

        private final Insets padding;

        RoundedBorder(
                int topLeftRadius,
                int topRightRadius,
                int bottomRightRadius,
                int bottomLeftRadius,
                Color borderColor,
                int thickness,
                int paddingTop,
                int paddingRight,
                int paddingBottom,
                int paddingLeft) {

            this.topLeftRadius = Math.max(0, topLeftRadius);
            this.topRightRadius = Math.max(0, topRightRadius);
            this.bottomRightRadius = Math.max(0, bottomRightRadius);
            this.bottomLeftRadius = Math.max(0, bottomLeftRadius);

            this.borderColor = borderColor;
            this.thickness = Math.max(0, thickness);

            this.padding = new Insets(
                    Math.max(0, paddingTop),
                    Math.max(0, paddingLeft),
                    Math.max(0, paddingBottom),
                    Math.max(0, paddingRight)
            );
        }

        // ==========================================
        // PAINT BORDER
        // ==========================================

        @Override
        public void paintBorder(
                Component component,
                Graphics g,
                int x,
                int y,
                int width,
                int height) {

            if (borderColor == null || thickness <= 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(thickness));

            float offset = thickness / 2f;

            Path2D borderPath = createRoundedPath(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    topLeftRadius,
                    topRightRadius,
                    bottomRightRadius,
                    bottomLeftRadius,
                    offset
            );

            g2.draw(borderPath);

            g2.dispose();
        }

        // ==========================================
        // BORDER INSETS
        // ==========================================

        @Override
        public Insets getBorderInsets(Component component) {

            return new Insets(
                    thickness + padding.top,
                    thickness + padding.left,
                    thickness + padding.bottom,
                    thickness + padding.right
            );
        }

        @Override
        public Insets getBorderInsets(
                Component component,
                Insets insets) {

            insets.set(
                    thickness + padding.top,
                    thickness + padding.left,
                    thickness + padding.bottom,
                    thickness + padding.right
            );

            return insets;
        }
    }
}
