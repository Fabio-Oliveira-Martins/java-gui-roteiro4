package model;

import java.util.Objects;

public class Book {
    private final String title;
    private final Author author;
    private final String publisher;
    private final Category category;
    private final Long isbn;
    private final String description; // opcional: nunca é null, no máximo ""
    private final boolean rented;

    public Book(String title, Author author, String publisher, Category category, Long isbn, String description) {
        this(title, author, publisher, category, isbn, description, false);
    }

    public Book(String title, Author author, String publisher, Category category,
                Long isbn, String description, boolean rented) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (publisher == null || publisher.isBlank()) {
            throw new IllegalArgumentException("Publisher is required");
        }

        this.title = title.trim();
        this.author = Objects.requireNonNull(author, "Author is required");
        this.publisher = publisher.trim();
        this.category = Objects.requireNonNull(category, "Category is required");
        this.isbn = Objects.requireNonNull(isbn, "ISBN is required");
        this.description = description == null ? "" : description.trim();
        this.rented = rented;
    }

    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public Category getCategory() { return category; }
    public Long getIsbn() { return isbn; }
    public String getDescription() { return description; }
    public boolean isRented() { return rented; }

    // O ISBN identifica o livro
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book other)) return false;
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "\"" + title + "\" - " + author
                + " | " + publisher
                + " | " + category
                + " | ISBN " + isbn
                + (description.isEmpty() ? "" : " | " + description);
    }
}