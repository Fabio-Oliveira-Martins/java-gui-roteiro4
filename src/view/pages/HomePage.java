package view.pages;

import model.BookRepository;
import view.components.sections.Section;
import view.components.sections.BookRowSection;
import view.components.sections.CategoriesSection;
import view.components.sections.DashboardSection;

import java.util.List;

public class HomePage extends Page {
    private final BookRepository repository;

    public HomePage(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    protected List<Section> createSections() {
        return List.of(
                new DashboardSection(repository),
                BookRowSection.recentlyAdded(repository, this::render),
                BookRowSection.suggestion(repository, this::render),
                new CategoriesSection(repository)
        );
    }
}