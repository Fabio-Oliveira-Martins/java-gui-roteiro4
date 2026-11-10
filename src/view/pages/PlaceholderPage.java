package view.pages;

import view.components.sections.Section;
import java.util.List;

public class PlaceholderPage extends Page {
    private final String title;

    public PlaceholderPage(String title) {
        this.title = title;
    }

    @Override
    protected List<Section> createSections() {
        return List.of(new Section(title));
    }
}