package model;

public enum Category {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    CHILDREN_AND_YOUNG_ADULT("Children"),
    ACADEMIC("Academic"),
    POETRY_AND_DRAMA("Poetry & Drama"),
    REFERENCE("Reference");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Converte o actionCommand do CustomInputRadio (label em minúsculas)
    public static Category fromActionCommand(String actionCommand) {
        if (actionCommand == null) {
            throw new IllegalArgumentException("Category is required");
        }
        for (Category category : values()) {
            if (category.displayName.equalsIgnoreCase(actionCommand.trim())) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + actionCommand);
    }

    @Override
    public String toString() {
        return displayName;
    }
}