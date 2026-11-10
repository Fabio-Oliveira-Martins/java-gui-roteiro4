package view.components;

import model.Book;
import model.BookRepository;
import persistence.DataAccessException;
import view.components.customDialogs.BookDetailsDialog;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class BookCard extends JButton implements Themeable {
    public enum Size {
        LARGE(120, 1.5, new Color(204, 180, 220)),  // capa 2:3
        SMALL(110, 1.2, new Color(182, 187, 219));

        public final int minWidth;     // menor largura do card
        public final double ratio;     // altura da capa = largura * ratio
        public final Color coverColor;

        Size(int minWidth, double ratio, Color coverColor) {
            this.minWidth = minWidth;
            this.ratio = ratio;
            this.coverColor = coverColor;
        }
    }

    private static final int TEXT_HEIGHT = 40;
    private static final Font TITLE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();
    private static final Font AUTHOR_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 12f).getFont();

    private final Size size;
    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel authorLabel = new JLabel("", SwingConstants.CENTER);

    public BookCard(Book book, Size size, BookRepository repository, Runnable onChange) {
        this.size = size;

        setLayout(new BorderLayout(0, 6));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setText(book.getTitle());
        titleLabel.setFont(TITLE_FONT);
        authorLabel.setText(book.getAuthor().getName());
        authorLabel.setFont(AUTHOR_FONT);

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        text.add(titleLabel);
        text.add(authorLabel);

        add(new BookCover(size.coverColor), BorderLayout.CENTER);
        add(text, BorderLayout.SOUTH);

        addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            try {
                new BookDetailsDialog(window, book, repository, onChange).open();
            } catch (DataAccessException ex) {
                ErrorDialog.display(window, ex.getMessage());
            }
        });

        applyTheme(ThemeManager.current());
    }

    // Altura total do card para uma largura (capa + texto)
    public static int heightFor(Size size, int width) {
        return (int) (width * size.ratio) + TEXT_HEIGHT;
    }

    @Override
    public void applyTheme(Theme theme) {
        titleLabel.setForeground(theme.text);
        authorLabel.setForeground(theme.textMuted);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size.minWidth, heightFor(size, size.minWidth));
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}