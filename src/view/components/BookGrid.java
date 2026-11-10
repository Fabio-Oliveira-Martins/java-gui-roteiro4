package view.components;

import javax.swing.*;
import java.awt.*;

public class BookGrid extends JPanel {
    private static final int GAP = 16;
    private static final int MAX_COLUMNS = 6;

    private final BookCard.Size size;
    private int lastHeight = -1;

    public BookGrid(BookCard.Size size) {
        this.size = size;
        setOpaque(false);
        setLayout(null); // posicionamento em doLayout
    }

    private int columns() {
        int width = Math.max(getWidth(), size.minWidth);
        return Math.max(1, Math.min(MAX_COLUMNS, (width + GAP) / (size.minWidth + GAP)));
    }

    private int cardWidth(int columns) {
        int width = Math.max(getWidth(), size.minWidth);
        return Math.max(size.minWidth, (width - (columns - 1) * GAP) / columns);
    }

    @Override
    public Dimension getPreferredSize() {
        int count = getComponentCount();
        if (count == 0) return new Dimension(0, 0);

        int columns = columns();
        int rows = (count + columns - 1) / columns;
        int rowHeight = BookCard.heightFor(size, cardWidth(columns));
        return new Dimension(size.minWidth, rows * rowHeight + (rows - 1) * GAP);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, getPreferredSize().height); // encolhe na largura, nunca na altura
    }

    @Override
    public void doLayout() {
        if (getComponentCount() == 0) return;

        int columns = columns();
        int width = cardWidth(columns);
        int height = BookCard.heightFor(size, width);

        for (int i = 0; i < getComponentCount(); i++) {
            getComponent(i).setBounds(
                    (i % columns) * (width + GAP),   // sempre a partir da esquerda
                    (i / columns) * (height + GAP),
                    width, height);
        }

        // a altura depende da largura: se mudou, pede novo layout DEPOIS da validação atual
        int preferred = getPreferredSize().height;
        if (preferred != lastHeight) {
            lastHeight = preferred;
            SwingUtilities.invokeLater(this::revalidate);
        }
    }
}