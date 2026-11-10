package view.components.sections;

import model.BookRepository;
import view.components.AdaptiveGrid;
import view.components.StatCard;

import java.awt.Color;

public class DashboardSection extends Section {

    public DashboardSection(BookRepository repository) {
        super("Dashboard");

        StatCard authors = new StatCard("authors.png", new Color(0, 142, 255),
                repository.countAuthors(), "Authors");
        StatCard books = new StatCard("cube.png", new Color(255, 165, 0),
                repository.findAll().size(), "Total Of Books");
        StatCard rented = new StatCard("bookmark-light.png", new Color(255, 71, 71),
                repository.findRented().size(), "Rented Books");

        AdaptiveGrid grid = new AdaptiveGrid(200, 2, 3, 16, true) {
            @Override
            public void doLayout() {
                authors.setVisible(columnsFor(getWidth()) >= 3); // sem espaço para 3: some "Authors"
                super.doLayout();
            }
        };
        grid.add(authors);
        grid.add(books);
        grid.add(rented);

        setContent(grid);
    }
}