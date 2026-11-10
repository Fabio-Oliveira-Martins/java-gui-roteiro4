package view.components.sections;

import model.Category;
import utils.IconLoader;
import view.components.CategoryStyle;
import view.components.sections.Section;
import view.components.SearchBar;
import view.components.componentsUtils.RoundedCorners;
import view.themes.ThemeManager;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExploreSection extends Section {
    private static final int BASE_WIDTH = 480;   // tamanho original (escala 1.0)
    private static final int MAX_WIDTH = 800;
    private static final int WIDTH_PERCENT = 60; // % da largura da seção
    private static final int STEP = 40;          // a escala muda em degraus
    private static final int BASE_GAP = 12;

    private final SearchBar searchBar = new SearchBar();
    private final ButtonGroup group = new ButtonGroup();
    private final List<CategoryButton> buttons = new ArrayList<>();
    private Category selected;
    private Runnable onFilterChange = () -> {};
    private Runnable onSearch = () -> {};

    public ExploreSection() {
        super("Explore Our Catalog");
        setTitleAlignment(SwingConstants.CENTER);

        GridLayout gridLayout = new GridLayout(2, 3, BASE_GAP, BASE_GAP); // 2 linhas x 3 colunas
        JPanel buttonGrid = new JPanel(gridLayout);
        buttonGrid.setOpaque(false);

        for (Category category : Category.values()) {
            CategoryButton button = new CategoryButton(category);
            button.addActionListener(e -> {
                if (selected == category) { // clicar de novo desmarca
                    group.clearSelection();
                    selected = null;
                } else {
                    selected = category;
                }
                onFilterChange.run();
                onSearch.run();
            });
            group.add(button);
            buttons.add(button);
            buttonGrid.add(button);
        }

        searchBar.setOnChange(() -> onFilterChange.run());
        searchBar.setOnSearch(text -> onSearch.run());

        JPanel content = new JPanel(new GridBagLayout()); // centraliza o bloco
        content.setOpaque(false);
        content.add(new ScaledBlock(buttonGrid, gridLayout));
        setContent(content);
    }

    public String getSearchText() { return searchBar.getText(); }
    public Category getSelectedCategory() { return selected; }

    // Chamado a cada mudança (digitação ou categoria): mostra/oculta as seções de destaque
    public void setOnFilterChange(Runnable onFilterChange) { this.onFilterChange = onFilterChange; }

    // Chamado ao clicar na lupa, apertar Enter ou marcar/desmarcar uma categoria
    public void setOnSearch(Runnable onSearch) { this.onSearch = onSearch; }

    public boolean isFilterActive() {
        return !searchBar.getText().isEmpty() || selected != null;
    }

    // Desabilita a ação durante a busca
    public void setSearching(boolean searching) {
        searchBar.setActionEnabled(!searching);
        for (CategoryButton button : buttons) {
            button.setEnabled(!searching);
        }
    }

    // Bloco (busca + botões) cuja largura e escala acompanham a seção
    private class ScaledBlock extends JPanel {
        private final BorderLayout borderLayout = new BorderLayout(0, BASE_GAP);
        private final GridLayout gridLayout;
        private int appliedWidth = -1;
        private int lastHeight = -1;

        ScaledBlock(JPanel buttonGrid, GridLayout gridLayout) {
            this.gridLayout = gridLayout;
            setOpaque(false);
            setLayout(borderLayout);
            add(searchBar, BorderLayout.NORTH);
            add(buttonGrid, BorderLayout.CENTER);
        }

        private int targetWidth() {
            Container parent = getParent();
            int available = parent == null ? 0 : parent.getWidth();
            int width = Math.max(BASE_WIDTH, Math.min(MAX_WIDTH, available * WIDTH_PERCENT / 100));
            return BASE_WIDTH + (width - BASE_WIDTH) / STEP * STEP;
        }

        private void applyScale(int width) {
            if (width == appliedWidth) return;
            appliedWidth = width;

            double scale = (double) width / BASE_WIDTH;
            int gap = (int) (BASE_GAP * scale);
            borderLayout.setVgap(gap);
            gridLayout.setHgap(gap);
            gridLayout.setVgap(gap);

            searchBar.scaleTo(scale);
            for (CategoryButton button : buttons) {
                button.scaleTo(scale);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int width = targetWidth();
            applyScale(width);
            return new Dimension(width, super.getPreferredSize().height);
        }

        @Override
        public void doLayout() {
            super.doLayout();
            int height = getPreferredSize().height; // a altura depende da largura
            if (height != lastHeight) {
                lastHeight = height;
                SwingUtilities.invokeLater(this::revalidate);
            }
        }
    }

    private static class CategoryButton extends JToggleButton {
        private static final int RADIUS = 10;
        private static final Font FONT = new LocalFont("Nunito", FontWeight.BOLD, 11f).getFont();
        private final Color color;
        private final String iconName;

        CategoryButton(Category category) {
            super(category.getDisplayName());
            color = CategoryStyle.color(category);
            iconName = CategoryStyle.icon(category);

            setVerticalTextPosition(SwingConstants.BOTTOM);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setForeground(Color.WHITE);

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            scaleTo(1.0);
        }

        // Fonte, ícone, espaçamento e altura acompanham a escala
        void scaleTo(double scale) {
            setFont(FONT.deriveFont((float) (11 * scale)));
            setIcon(IconLoader.load(iconName, (int) (20 * scale)));
            setIconTextGap((int) (4 * scale));
            setPreferredSize(new Dimension(0, (int) (60 * scale)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (!isEnabled()) { // desabilitado durante a busca: aparência esmaecida
                g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            }

            RoundedCorners.fillRoundedRectangle(g2, color, 0, 0, getWidth(), getHeight(), RADIUS);

            if (isSelected()) { // anel de seleção na cor do texto do tema
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.current().text);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, RADIUS, RADIUS);
            }

            super.paintComponent(g2);
            g2.dispose();
        }
    }
}