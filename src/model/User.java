package model;

public class User {
    private String username;
    private String password; // com banco de dados, guardar o hash em vez do texto

    public User(String username, String password) {
        this.username = requireText(username, "Username");
        this.password = requireText(password, "Password");
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = requireText(username, "Username"); }
    public void setPassword(String password) { this.password = requireText(password, "Password"); }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}