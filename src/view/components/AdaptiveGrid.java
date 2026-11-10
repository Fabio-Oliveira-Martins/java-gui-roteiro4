package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdaptiveGrid extends JPanel {
    private final int minCellWidth;
    private final int minColumns;
    private final int maxColumns;
    private final int gap;
    private final boolean stretch; // true: célula ocupa todo o espaço; false: item mantém o tamanho e fica centralizado
    private int lastHeight = -1;

    public AdaptiveGrid(int minCellWidth, int minColumns, int maxColumns, int gap, boolean stretch) {
        this.minCellWidth = minCellWidth;
        this.minColumns = minColumns;
        this.maxColumns = maxColumns;
        this.gap = gap;
        this.stretch = stretch;
        setOpaque(false);
        setLayout(null); // o posicionamento é feito em doLayout
    }

    public int columnsFor(int width) {
        int columns = (width + gap) / (minCellWidth + gap);
        return Math.max(minColumns, Math.min(maxColumns, columns));
    }

    private List<Component> items() {
        List<Component> items = new ArrayList<>();
        for (Component c : getComponents()) {
            if (c.isVisible()) items.add(c);
        }
        return items;
    }

    private int rowHeight(List<Component> items) {
        int height = 0;
        for (Component c : items) height = Math.max(height, c.getPreferredSize().height);
        return height;
    }

    @Override
    public Dimension getPreferredSize() {
        List<Component> items = items();
        if (items.isEmpty()) return new Dimension(0, 0);

        int columns = Math.min(columnsFor(getWidth()), items.size());
        int rows = (items.size() + columns - 1) / columns;
        return new Dimension(minCellWidth, rows * rowHeight(items) + (rows - 1) * gap);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, getPreferredSize().height); // permite encolher na largura
    }

    @Override
    public void doLayout() {
        List<Component> items = items();
        if (items.isEmpty()) return;

        int columns = Math.min(columnsFor(getWidth()), items.size());
        int rowHeight = rowHeight(items);

        for (int i = 0; i < items.size(); i++) {
            Component item = items.get(i);
            int column = i % columns;
            int y = (i / columns) * (rowHeight + gap);

            if (stretch) {
                int cell = (getWidth() - (columns - 1) * gap) / columns;
                item.setBounds(column * (cell + gap), y, cell, rowHeight);
            } else {
                int slot = getWidth() / columns;
                int width = item.getPreferredSize().width;
                item.setBounds(column * slot + (slot - width) / 2, y, width, rowHeight);
            }
        }

        // A altura depende das colunas: se mudou, pede novo layout DEPOIS da validação atual
        int preferred = getPreferredSize().height;
        if (preferred != lastHeight) {
            lastHeight = preferred;
            SwingUtilities.invokeLater(this::revalidate);
        }
    }
}