package view.components.sections;

import model.Book;
import model.BookRepository;
import view.components.BookCard;
import view.components.BookGrid;
import view.components.MutedLabel;
import view.components.sections.Section;

import java.util.List;

public class BooksSection extends Section {
    private static final String EMPTY_MESSAGE = "No books found.";

    private final BookRepository repository;
    private final Runnable onChange;

    public BooksSection(String title, List<Book> books, BookRepository repository, Runnable onChange) {
        super(title);
        this.repository = repository;
        this.onChange = onChange;
        showBooks(books);
    }

    // Troca a lista exibida (usado também pelo filtro da Explore)
    public final void showBooks(List<Book> books) {
        if (books.isEmpty()) {
            setContent(new MutedLabel(EMPTY_MESSAGE));
            return;
        }

        BookGrid grid = new BookGrid(BookCard.Size.SMALL);
        for (Book book : books) {
            grid.add(new BookCard(book, BookCard.Size.SMALL, repository, onChange));
        }
        setContent(grid);
    }
}