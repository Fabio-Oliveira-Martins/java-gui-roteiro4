package model;

import persistence.jdbc.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static final int DUPLICATE_ENTRY = 1062; // código do banco para chave única repetida
    private static final String CATEGORY_ID = "(SELECT id FROM categories WHERE name = ?)";
    private static final String SELECT_BOOKS = """
            SELECT b.isbn, b.title, a.name AS author, b.publisher,
                   c.name AS category, b.description, b.rented
              FROM books b
              JOIN authors a ON a.id = b.author_id
              JOIN categories c ON c.id = b.category_id
            """;

    private final Database database;

    public BookRepository(Database database) {
        this.database = database;
    }

    // ===== Etapa 2: leitura (consultas parametrizadas) =====

    public List<Book> findAll() {
        return search(null, null);
    }

    // Título contém o texto e/ou categoria igual. Nulo ou vazio = sem filtro.
    public List<Book> search(String title, Category category) {
        StringBuilder sql = new StringBuilder(SELECT_BOOKS).append(" WHERE 1 = 1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            sql.append(" AND b.title LIKE ?");
            params.add("%" + escapeLike(title.trim()) + "%");
        }
        if (category != null) {
            sql.append(" AND c.name = ?");
            params.add(category.name());
        }
        sql.append(" ORDER BY b.id");
        return query(sql.toString(), params.toArray());
    }

    public List<Book> random(int limit) {
        return query(SELECT_BOOKS + " ORDER BY RAND() LIMIT ?", limit);
    }

    public List<Book> recent(int limit) {
        return query(SELECT_BOOKS + " ORDER BY b.id DESC LIMIT ?", limit);
    }

    public List<Book> findRented() {
        return query(SELECT_BOOKS + " WHERE b.rented = TRUE ORDER BY b.id");
    }

    public int countAuthors() {
        return count("SELECT COUNT(DISTINCT author_id) FROM books");
    }

    public int countByCategory(Category category) {
        return count("SELECT COUNT(*) FROM books b JOIN categories c ON c.id = b.category_id WHERE c.name = ?",
                category.name());
    }

    // ===== Etapa 4: transação com duas tabelas (authors e books) =====

    // Grava o autor (se novo) e o livro como uma unidade: ou os dois, ou nenhum.
    // Retorna false se já existe livro com esse ISBN.
    public boolean add(Book book) {
        try (Connection connection = database.open()) {                       // fecha a conexão sempre
            try {
                connection.setAutoCommit(false);                              // início da transação
                long authorId = findOrCreateAuthor(connection, book.getAuthor()); // comando 1 (authors)
                insertBook(connection, book, authorId);                       // comando 2 (books)
                connection.commit();                                          // só aqui os dois valem
                return true;
            } catch (SQLException | RuntimeException e) {
                rollback(connection);                                         // desfaz o que foi feito
                throw e;
            } finally {
                connection.setAutoCommit(true);                               // restaura o estado esperado
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == DUPLICATE_ENTRY) return false;            // ISBN repetido
            throw Database.failure("Could not save the book", e);
        } catch (SQLException e) {
            throw Database.failure("Could not save the book", e);
        }
    }

    // ===== Etapa 3: atualização e exclusão (true = 1 linha afetada, false = ISBN inexistente) =====

    public boolean update(Book book) {
        return execute("Could not update the book",
                "UPDATE books SET title = ?, publisher = ?, description = ?, category_id = " + CATEGORY_ID
                        + " WHERE isbn = ?",
                book.getTitle(), book.getPublisher(), book.getDescription(),
                book.getCategory().name(), isbn(book)) == 1;
    }

    public boolean remove(Book book) {
        try (Connection connection = database.open()) {
            try {
                connection.setAutoCommit(false);

                // Recupera o ID do autor e exclui o livro.
                long authorId;

                try (PreparedStatement statement = prepare(connection,
                        "SELECT author_id FROM books WHERE isbn = ?",
                        isbn(book));
                     ResultSet rows = statement.executeQuery()) {

                    if (!rows.next()) {
                        connection.rollback();
                        return false;
                    }

                    authorId = rows.getLong("author_id");
                }

                // Exclui o livro.
                try (PreparedStatement statement = prepare(connection,
                        "DELETE FROM books WHERE isbn = ?",
                        isbn(book))) {

                    requireOneRow(statement.executeUpdate());
                }

                // Verifica se o autor ainda possui livros cadastrados.
                try (PreparedStatement statement = prepare(connection,
                        "SELECT COUNT(*) FROM books WHERE author_id = ?",
                        authorId);
                     ResultSet rows = statement.executeQuery()) {

                    if (rows.next() && rows.getInt(1) == 0) {

                        // Exclui o autor caso não possua outros livros.
                        try (PreparedStatement deleteAuthor = prepare(connection,
                                "DELETE FROM authors WHERE id = ?",
                                authorId)) {

                            deleteAuthor.executeUpdate();
                        }
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                rollback(connection);
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw Database.failure("Could not delete the book", e);
        }
    }

    public boolean rent(Book book) {
        return setRented(book, true);
    }

    public boolean unrent(Book book) {
        return setRented(book, false);
    }

    private boolean setRented(Book book, boolean rented) {
        return execute("Could not update the book",
                "UPDATE books SET rented = ? WHERE isbn = ?", rented, isbn(book)) == 1;
    }

    // ===== Infraestrutura JDBC =====

    private List<Book> query(String sql, Object... params) {
        try (Connection connection = database.open();
             PreparedStatement statement = prepare(connection, sql, params);
             ResultSet rows = statement.executeQuery()) {
            List<Book> books = new ArrayList<>();
            while (rows.next()) {
                books.add(toBook(rows));
            }
            return books;
        } catch (SQLException e) {
            throw Database.failure("Could not load books", e);
        }
    }

    private int count(String sql, Object... params) {
        try (Connection connection = database.open();
             PreparedStatement statement = prepare(connection, sql, params);
             ResultSet rows = statement.executeQuery()) {
            return rows.next() ? rows.getInt(1) : 0;
        } catch (SQLException e) {
            throw Database.failure("Could not read book data", e);
        }
    }

    // Executa um comando e devolve a contagem de linhas afetadas
    private int execute(String errorMessage, String sql, Object... params) {
        try (Connection connection = database.open();
             PreparedStatement statement = prepare(connection, sql, params)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw Database.failure(errorMessage, e);
        }
    }

    private static PreparedStatement prepare(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement;
    }

    // Linha do banco -> entidade (sem depender de Swing)
    private static Book toBook(ResultSet row) throws SQLException {
        return new Book(
                row.getString("title"),
                new Author(row.getString("author")),
                row.getString("publisher"),
                Category.valueOf(row.getString("category")),
                Long.parseLong(row.getString("isbn")),
                row.getString("description"),
                row.getBoolean("rented"));
    }

    // Reaproveita o autor existente ou cria um novo, recuperando o id gerado
    private long findOrCreateAuthor(Connection connection, Author author) throws SQLException {
        try (PreparedStatement find = prepare(connection, "SELECT id FROM authors WHERE name = ?", author.getName());
             ResultSet rows = find.executeQuery()) {
            if (rows.next()) return rows.getLong(1);
        }

        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO authors (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, author.getName());
            requireOneRow(insert.executeUpdate());

            try (ResultSet keys = insert.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Generated key not returned");
                return keys.getLong(1);
            }
        }
    }

    private void insertBook(Connection connection, Book book, long authorId) throws SQLException {
        try (PreparedStatement insert = prepare(connection,
                "INSERT INTO books (isbn, title, author_id, publisher, category_id, description) "
                        + "VALUES (?, ?, ?, ?, " + CATEGORY_ID + ", ?)",
                isbn(book), book.getTitle(), authorId, book.getPublisher(),
                book.getCategory().name(), book.getDescription())) {
            requireOneRow(insert.executeUpdate());
        }
    }

    // Confere as linhas afetadas antes de considerar o comando um sucesso
    private static void requireOneRow(int affected) throws SQLException {
        if (affected != 1) throw new SQLException("Expected 1 affected row but got " + affected);
    }

    private static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.printf("[DB] rollback failed (SQLState=%s)%n", e.getSQLState());
        }
    }

    private static String isbn(Book book) {
        return String.valueOf(book.getIsbn());
    }

    // Os curingas do LIKE digitados pelo usuário viram texto comum
    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}