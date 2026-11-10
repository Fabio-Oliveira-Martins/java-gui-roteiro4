package view.components;

import model.Category;

import java.awt.Color;

public final class CategoryStyle {
    private CategoryStyle() {}

    public static Color color(Category category) {
        return switch (category) {
            case FICTION -> new Color(80, 55, 140);
            case NON_FICTION -> new Color(107, 76, 52);
            case CHILDREN_AND_YOUNG_ADULT -> new Color(255, 122, 0);
            case ACADEMIC -> new Color(112, 143, 168);
            case POETRY_AND_DRAMA -> new Color(255, 112, 150);
            case REFERENCE -> new Color(52, 58, 64);
        };
    }

    public static String icon(Category category) {
        return switch (category) {
            case FICTION -> "open-book.png";
            case NON_FICTION -> "book-check.png";
            case CHILDREN_AND_YOUNG_ADULT -> "blocks.png";
            case ACADEMIC -> "academic-cap.png";
            case POETRY_AND_DRAMA -> "feather.png";
            case REFERENCE -> "dictionary.png";
        };
    }
}