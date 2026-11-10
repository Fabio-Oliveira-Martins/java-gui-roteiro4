package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookRepository {
    private final List<Book> books = new CopyOnWriteArrayList<>();
    private final Set<Book> rented = new LinkedHashSet<>();

    // Retorna false se já existir um livro com o mesmo ISBN
    public boolean add(Book book) {
        if (books.contains(book)) {
            return false;
        }
        books.add(book);
        return true;
    }

    // Reaproveita o autor já cadastrado, se existir
    public Author resolveAuthor(String name) {
        Author candidate = new Author(name);
        for (Book book : books) {
            if (book.getAuthor().equals(candidate)) {
                return book.getAuthor();
            }
        }
        return candidate;
    }

    public List<Book> findAll() {
        return List.copyOf(books);
    }

    // Título contém o texto (sem diferenciar maiúsculas) e/ou categoria igual. Nulo ou vazio = sem filtro.
    public List<Book> search(String title, Category category) {
        String term = title == null ? "" : title.trim().toLowerCase();
        return books.stream()
                .filter(b -> term.isEmpty() || b.getTitle().toLowerCase().contains(term))
                .filter(b -> category == null || b.getCategory() == category)
                .toList();
    }

    public void printAll() {
        if (books.isEmpty()) {
            System.out.println("No books registered.");
            return;
        }

        System.out.println("===== Books (" + books.size() + ") =====");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    public boolean remove(Book book) {
        rented.remove(book);
        return books.remove(book);
    }

    public void rent(Book book) { rented.add(book); }
    public void unrent(Book book) { rented.remove(book); }
    public boolean isRented(Book book) { return rented.contains(book); }
    public List<Book> findRented() { return List.copyOf(rented); }

    // Livros sorteados, no máximo "limit"
    public List<Book> random(int limit) {
        List<Book> copy = new ArrayList<>(books);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(limit, copy.size()));
    }

    // Últimos livros adicionados, do mais novo para o mais antigo
    public List<Book> recent(int limit) {
        int from = Math.max(0, books.size() - limit);
        List<Book> last = new ArrayList<>(books.subList(from, books.size()));
        Collections.reverse(last);
        return last;
    }

    public int countAuthors() {
        return (int) books.stream().map(Book::getAuthor).distinct().count();
    }

    public int countByCategory(Category category) {
        return (int) books.stream().filter(b -> b.getCategory() == category).count();
    }
}