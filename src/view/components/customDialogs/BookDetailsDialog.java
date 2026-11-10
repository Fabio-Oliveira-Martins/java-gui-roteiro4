package view.components.customDialogs;

import model.Book;
import model.BookRepository;
import persistence.DataAccessException;
import view.components.BookCover;
import view.components.CategoryStyle;
import view.components.componentsUtils.CustomScrollBarUI;
import view.components.componentsUtils.RoundedCorners;
import view.components.customButtons.IconButton;
import view.components.customButtons.SecondaryButton;
import view.components.customButtons.SubmitButton;
import view.components.customDialogs.statusDialog.AlertDialog;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.customDialogs.statusDialog.SuccessDialog;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BookDetailsDialog extends JDialog {
    private static final int LARGE_LAYOUT_MIN_WINDOW_WIDTH = 1000; // abaixo disso: coluna única
    private static final int LARGE_MAX_WIDTH = 820;
    private static final int SMALL_MAX_WIDTH = 440;
    private static final int PADDING = 32;
    private static final int GAP = 24;
    private static final int RADIUS = 16;
    private static final int BORDER = 2;
    private static final int MAIN_BAR = 8;                 // largura da barra do dialog (sempre reservada)
    private static final int DESCRIPTION_BAR = 6;          // largura da barra da descrição (sempre reservada)
    private static final int WINDOW_MARGIN = 60;           // folga entre o dialog e a janela
    private static final int DESCRIPTION_MAX_HEIGHT = 120; // acima disso a descrição rola
    private static final double COVER_RATIO = 1.4;         // altura = largura * 1,4
    private static final String CLOSE_ICON = "close.png";
    private static final Color COVER_COLOR = new Color(204, 180, 220);

    private static final Font TITLE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 20f).getFont();
    private static final Font LABEL_FONT = new LocalFont("Nunito", FontWeight.BOLD, 13f).getFont();
    private static final Font TEXT_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 13f).getFont();

    private final Theme theme = ThemeManager.current();
    private final Book book;
    private final BookRepository repository;
    private final Runnable onChange;
    private final int dialogWidth;
    private final int maxHeight;

    public BookDetailsDialog(Window owner, Book book, BookRepository repository, Runnable onChange) {
        super(owner, Dialog.ModalityType.APPLICATION_MODAL);
        this.book = book;
        this.repository = repository;
        this.onChange = onChange;

        boolean large = owner.getWidth() >= LARGE_LAYOUT_MIN_WINDOW_WIDTH;
        dialogWidth = large
                ? Math.min(LARGE_MAX_WIDTH, owner.getWidth() - 160)
                : Math.min(SMALL_MAX_WIDTH, owner.getWidth() - 40);
        maxHeight = owner.getHeight() - WINDOW_MARGIN;

        // Largura exata do conteúdo: dialog - bordas - barra reservada - padding
        int contentWidth = dialogWidth - 2 * BORDER - MAIN_BAR - 2 * PADDING;

        setUndecorated(true);
        setResizable(false);

        JPanel layout;
        if (large) {
            int coverWidth = contentWidth * 2 / 5;
            layout = largeLayout(coverWidth, contentWidth - coverWidth - GAP);
        } else {
            layout = smallLayout(contentWidth);
        }

        // +4 à esquerda e -4 à direita compensam a barra reservada: o conteúdo parece centralizado
        Content content = new Content(layout);
        content.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING + 4, PADDING, PADDING - 4));

        JScrollPane scroll = scrollPane(content, MAIN_BAR, 4);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                // largura fixa; altura do conteúdo, limitada à altura da janela
                return new Dimension(dialogWidth, Math.min(super.getPreferredSize().height, maxHeight));
            }

            @Override
            protected void paintComponent(Graphics g) {
                RoundedCorners.fillRoundedRectangle(g, theme.surface, 0, 0, getWidth(), getHeight(), RADIUS);
            }
        };
        root.setOpaque(false);
        RoundedCorners.apply(root, RADIUS, theme.border, BORDER, 0);
        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);

        getRootPane().registerKeyboardAction(
                e -> dispose(), KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void open() {
        pack();
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), RADIUS * 2, RADIUS * 2));
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    // Conteúdo rolável: acompanha a largura do viewport
    private static class Content extends JPanel implements Scrollable {
        Content(JComponent child) {
            setOpaque(false);
            setLayout(new BorderLayout());
            add(child, BorderLayout.CENTER);
        }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return r.height; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }

        // Se o viewport for mais alto que o conteúdo, estica (os botões ficam embaixo)
        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport v && v.getHeight() > getPreferredSize().height;
        }
    }

    // ===== Layouts =====

    private JPanel largeLayout(int coverWidth, int textWidth) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(closeButton(), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, GAP);
        panel.add(cover(coverWidth), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(infoPanel(textWidth), gbc);
        return panel;
    }

    // Uma coluna: todos os elementos têm a mesma largura; só a capa é centralizada
    private JPanel smallLayout(int width) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(aligned(closeButton(), FlowLayout.LEFT), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(aligned(cover(200), FlowLayout.CENTER), gbc);

        gbc.gridy = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(infoPanel(width), gbc);
        return panel;
    }

    // Capa com tamanho mínimo = preferido: nunca é comprimida
    private static BookCover cover(int width) {
        BookCover cover = new BookCover(COVER_COLOR);
        Dimension size = new Dimension(width, (int) (width * COVER_RATIO));
        cover.setPreferredSize(size);
        cover.setMinimumSize(size);
        return cover;
    }

    // Coluna de informações, igual nos dois layouts
    private JPanel infoPanel(int width) {
        String description = book.getDescription().isBlank() ? "No description provided." : book.getDescription();

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        info.add(text(book.getTitle(), TITLE_FONT, theme.text, width));
        info.add(Box.createVerticalStrut(2));
        info.add(text(book.getAuthor().getName(), TEXT_FONT, theme.textMuted, width));
        info.add(Box.createVerticalStrut(16));
        info.add(label("Description", LABEL_FONT, theme.text));
        info.add(Box.createVerticalStrut(6));
        info.add(descriptionBox(description, width));
        info.add(Box.createVerticalStrut(12));
        info.add(row("Genre:", chip(book.getCategory().getDisplayName(), CategoryStyle.color(book.getCategory())), width));
        info.add(Box.createVerticalStrut(4));
        info.add(row("Publisher:", null, width, book.getPublisher()));
        info.add(Box.createVerticalStrut(4));
        info.add(row("ISBN:", null, width, String.valueOf(book.getIsbn())));
        info.add(Box.createVerticalGlue()); // empurra os botões para baixo
        info.add(Box.createVerticalStrut(16));
        info.add(actions());

        for (Component c : info.getComponents()) {
            ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        return info;
    }

    // ===== Texto =====

    // JTextArea sem fundo nem borda, alinhado à esquerda, com quebra automática
    // (inclusive no meio de palavras longas) e largura fixa
    private static JTextArea text(String content, Font font, Color color, int width) {
        JTextArea area = new JTextArea(content);
        area.setFont(font);
        area.setForeground(color);
        area.setOpaque(false);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(null);
        area.setMargin(new Insets(0, 0, 0, 0));

        area.setSize(width, Short.MAX_VALUE); // a altura sai da quebra nessa largura
        Dimension size = new Dimension(width, area.getPreferredSize().height);
        area.setPreferredSize(size);
        area.setMinimumSize(size);
        area.setMaximumSize(size);
        return area;
    }

    // Descrição: altura limitada, com barra de rolagem vertical quando passa do limite
    private JScrollPane descriptionBox(String content, int width) {
        JTextArea area = new JTextArea(content);
        area.setFont(TEXT_FONT);
        area.setForeground(theme.textMuted);
        area.setOpaque(false);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(null);
        area.setMargin(new Insets(0, 0, 0, 0));

        area.setSize(width - DESCRIPTION_BAR, Short.MAX_VALUE);
        int height = Math.min(area.getPreferredSize().height, DESCRIPTION_MAX_HEIGHT);

        JScrollPane box = scrollPane(area, DESCRIPTION_BAR, 3);
        Dimension size = new Dimension(width, height);
        box.setPreferredSize(size);
        box.setMinimumSize(size);
        box.setMaximumSize(size);
        return box;
    }

    // Linha "nome: valor". O valor é um componente pronto (chip) ou um texto quebrado
    private JPanel row(String name, JComponent value, int width, String... textValue) {
        JLabel title = label(name, LABEL_FONT, theme.text);
        int titleWidth = title.getPreferredSize().width + 6;

        JComponent content = value != null
                ? value
                : text(textValue[0], TEXT_FONT, theme.textMuted, Math.max(40, width - titleWidth));

        title.setAlignmentY(Component.TOP_ALIGNMENT);
        content.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(title);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(content);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    // ===== Peças =====

    // A barra vertical é sempre reservada (track invisível), para a largura útil ser exata
    private JScrollPane scrollPane(JComponent view, int thickness, int radius) {
        JScrollPane scroll = new JScrollPane(view,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        Color c = theme.text;
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI(
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 70),
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 120),
                null, thickness, radius));
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel actions() {
        SubmitButton rent = new SubmitButton(book.isRented() ? "Return Book" : "Rent Book");
        rent.setPreferredSize(new Dimension(0, 40));
        rent.addActionListener(e -> toggleRent());

        SecondaryButton delete = SecondaryButton.danger("Delete Book");
        delete.setPreferredSize(new Dimension(0, 40));
        delete.addActionListener(e -> confirmDelete());

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.setOpaque(false);
        panel.add(rent);
        panel.add(delete);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private void toggleRent() {
        boolean wasRented = book.isRented();
        boolean done;
        try {
            done = wasRented ? repository.unrent(book) : repository.rent(book);
        } catch (DataAccessException e) {
            ErrorDialog.display(this, e.getMessage());
            return;
        }
        finish(done, wasRented ? "Book removed from your library" : "Book added to your library");
    }

    private void confirmDelete() {
        AlertDialog.confirm(this, "Delete Book",
                "Are you sure that you want to delete \"" + book.getTitle() + "\"?",
                () -> {
                    boolean removed;
                    try {
                        removed = repository.remove(book);
                    } catch (DataAccessException e) {
                        ErrorDialog.display(this, e.getMessage());
                        return;
                    }
                    finish(removed, "Book deleted successfully");
                });
    }

    // Sucesso (1 linha afetada) ou ausência (0 linhas): fecha, atualiza a página e informa
    private void finish(boolean done, String successMessage) {
        Window owner = getOwner();
        dispose();
        onChange.run();
        if (done) {
            SuccessDialog.display(owner, successMessage);
        } else {
            ErrorDialog.display(owner, "This book no longer exists");
        }
    }

    private JButton closeButton() {
        IconButton close = new IconButton(CLOSE_ICON, 20);
        close.addActionListener(e -> dispose());
        return close;
    }

    private static JPanel aligned(JComponent component, int alignment) {
        JPanel panel = new JPanel(new FlowLayout(alignment, 0, 0));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private static JLabel label(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private static JLabel chip(String text, Color color) {
        JLabel chip = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                RoundedCorners.fillRoundedRectangle(g, color, 0, 0, getWidth(), getHeight(), 999);
                super.paintComponent(g);
            }
        };
        chip.setFont(LABEL_FONT);
        chip.setForeground(Color.WHITE);
        chip.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 12));
        return chip;
    }
}