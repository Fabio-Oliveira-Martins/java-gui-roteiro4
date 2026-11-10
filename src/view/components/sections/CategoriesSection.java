package view.components.sections;

import model.BookRepository;
import model.Category;
import view.components.AdaptiveGrid;
import view.components.CategoryCard;

public class CategoriesSection extends Section {

    public CategoriesSection(BookRepository repository) {
        super("Categories Available");

        AdaptiveGrid grid = new AdaptiveGrid(200, 2, 3, 12, true);
        for (Category category : Category.values()) {
            grid.add(new CategoryCard(category, repository.countByCategory(category)));
        }
        setContent(grid);
    }
}