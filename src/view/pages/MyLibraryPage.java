package view.pages;

import model.BookRepository;
import view.components.sections.Section;
import view.components.sections.BooksSection;

import java.util.List;

public class MyLibraryPage extends Page {
    private final BookRepository repository;

    public MyLibraryPage(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    protected List<Section> createSections() {
        return List.of(new BooksSection("My Library", repository.findRented(), repository, this::render));
    }
}