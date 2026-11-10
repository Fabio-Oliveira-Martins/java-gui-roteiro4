package view.pages;

import model.BookRepository;
import view.components.BookForm;
import view.components.sections.Section;

import java.util.List;

public class AddBookPage extends Page {
    private final BookRepository repository;

    public AddBookPage(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    protected List<Section> createSections() {
        Section section = new Section("Add Book");
        section.setContent(new BookForm(repository));
        return List.of(section);
    }
}