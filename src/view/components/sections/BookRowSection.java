package view.components.sections;

import model.Book;
import model.BookRepository;
import view.components.BookCard;
import view.components.MutedLabel;
import view.components.sections.Section;
import view.components.componentsUtils.CustomScrollBarUI;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookRowSection extends Section {

    private BookRowSection(String title, List<Book> books, BookCard.Size size,
                           BookRepository repository, Runnable onChange) {
        super(title);
        setContent(books.isEmpty()
                ? new MutedLabel("No books available yet.")
                : new BookRow(books, size, repository, onChange));
    }

    // Até 5 livros sorteados
    public static BookRowSection recentlyAdded(BookRepository repository, Runnable onChange) {
        return new BookRowSection("Recently Added", repository.recent(5), BookCard.Size.LARGE, repository, onChange);
    }

    // Até 6 livros sorteados
    public static BookRowSection suggestion(BookRepository repository, Runnable onChange) {
        return new BookRowSection("Suggestion", repository.random(6), BookCard.Size.SMALL, repository, onChange);
    }
}

class BookRow extends JScrollPane implements Themeable {
    private static final int GAP = 16;
    private static final int BAR_HEIGHT = 10;

    private final BookCard.Size size;
    private final int count;
    private int lastHeight = -1;

    public BookRow(List<Book> books, BookCard.Size size, BookRepository repository, Runnable onChange) {
        super(new Row(books, size, repository, onChange),
                VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.size = size;
        this.count = books.size();

        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getHorizontalScrollBar().setUnitIncrement(32);

        applyTheme(ThemeManager.current());
    }

    private int cardWidth() {
        int width = Math.max(getWidth(), size.minWidth);
        return Math.max(size.minWidth, (width - (count - 1) * GAP) / count);
    }

    // Altura = card + espaço reservado para a barra. A largura vem do layout da seção.
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, BookCard.heightFor(size, cardWidth()) + BAR_HEIGHT);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize(); // evita o mínimo de ~4px do JScrollPane
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int preferred = getPreferredSize().height; // a altura depende da largura
        if (preferred != lastHeight) {
            lastHeight = preferred;
            SwingUtilities.invokeLater(this::revalidate);
        }
    }

    @Override
    public void applyTheme(Theme theme) {
        Color c = theme.text;
        getHorizontalScrollBar().setUI(new CustomScrollBarUI(
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 70),
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 120),
                null, BAR_HEIGHT, 5));
        getHorizontalScrollBar().setOpaque(false);
    }

    private static class Row extends JPanel implements Scrollable {
        private final BookCard.Size size;

        Row(List<Book> books, BookCard.Size size, BookRepository repository, Runnable onChange) {
            this.size = size;
            setOpaque(false);
            setLayout(null);
            for (Book book : books) {
                add(new BookCard(book, size, repository, onChange));
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int n = getComponentCount();
            return new Dimension(
                    n * size.minWidth + Math.max(0, n - 1) * GAP,    // largura mínima de todos
                    BookCard.heightFor(size, size.minWidth));
        }

        @Override
        public void doLayout() {
            int n = getComponentCount();
            if (n == 0) return;

            int width = Math.max(size.minWidth, (getWidth() - (n - 1) * GAP) / n);
            int height = BookCard.heightFor(size, width);

            for (int i = 0; i < n; i++) {
                getComponent(i).setBounds(i * (width + GAP), 0, width, height);
            }
        }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 32; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return r.width; }

        // Preenche o viewport quando há espaço; senão mantém a largura mínima e rola
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport v && v.getWidth() >= getPreferredSize().width;
        }

        // Altura acompanha o viewport (corrige as capas cortadas em tela cheia)
        @Override
        public boolean getScrollableTracksViewportHeight() {
            return true;
        }
    }
}