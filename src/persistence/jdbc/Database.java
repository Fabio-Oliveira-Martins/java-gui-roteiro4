package persistence.jdbc;

import persistence.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final DatabaseConfig config;

    public Database(DatabaseConfig config) {
        this.config = config;
    }

    // Quem chama fecha a conexão (try-with-resources)
    public Connection open() throws SQLException {
        return DriverManager.getConnection(config.url(), config.user(), config.password());
    }

    // Teste de conexão
    public void check() {
        try (Connection connection = open()) {
            if (!connection.isValid(3)) {
                throw new DataAccessException("The database connection is not valid");
            }
        } catch (SQLException e) {
            throw failure("Could not connect to the database", e);
        }
    }

    // SQLException -> falha da aplicação.
    // O log leva só SQLState e código (sem a mensagem do driver) e o texto público é genérico.
    public static DataAccessException failure(String message, SQLException e) {
        System.err.printf("[DB] %s (SQLState=%s, code=%d)%n", message, e.getSQLState(), e.getErrorCode());
        return new DataAccessException(message, e);
    }
}