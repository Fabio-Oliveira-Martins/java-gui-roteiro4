package model;

import java.util.Objects;

public class Author {
    private final String name;

    public Author(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name is required");
        }
        this.name = name.trim();
    }

    // Dois autores com o mesmo nome (ignorando maiúsculas) são o mesmo autor
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author other)) return false;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}