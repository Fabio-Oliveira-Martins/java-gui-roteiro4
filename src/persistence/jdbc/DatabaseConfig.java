package persistence.jdbc;

public record DatabaseConfig(String url, String user, String password) {

    public static DatabaseConfig fromEnvironment() {
        return new DatabaseConfig(
                required("BOOKS_DB_URL"),
                required("BOOKS_DB_USER"),
                System.getenv().getOrDefault("BOOKS_DB_PASSWORD", ""));
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + name);
        }
        return value;
    }

    // A senha nunca aparece em logs, mensagens ou no depurador
    @Override
    public String toString() {
        return "DatabaseConfig[user=" + user + ", password=****]";
    }
}