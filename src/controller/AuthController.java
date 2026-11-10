package controller;

import model.User;
import model.UserRepository;
import utils.PasswordValidator;

import java.util.Optional;
import java.util.function.Consumer;

public class AuthController {
    public enum LoginResult { SUCCESS, USER_NOT_FOUND, WRONG_PASSWORD }

    private final UserRepository repository;
    private User currentUser;
    private Consumer<User> onUserChanged = user -> {};

    public AuthController(UserRepository repository) {
        this.repository = repository;
    }

    // Chamado no login e quando o nome muda (ex.: para atualizar o Header)
    public void setOnUserChanged(Consumer<User> onUserChanged) {
        this.onUserChanged = onUserChanged;
    }

    public LoginResult login(String username, String password) {
        Optional<User> user = repository.findByUsername(username);

        if (user.isEmpty()) return LoginResult.USER_NOT_FOUND;
        if (!user.get().getPassword().equals(password)) return LoginResult.WRONG_PASSWORD;

        currentUser = user.get();
        onUserChanged.accept(currentUser);
        return LoginResult.SUCCESS;
    }

    public void logout() {
        currentUser = null;
    }

    // false se o nome já pertence a outro usuário
    public boolean changeUsername(String newUsername) {
        requireLoggedIn();

        Optional<User> existing = repository.findByUsername(newUsername.trim());
        if (existing.isPresent() && existing.get() != currentUser) {
            return false;
        }

        currentUser.setUsername(newUsername);
        repository.save(currentUser);
        onUserChanged.accept(currentUser);
        return true;
    }

    // false se a senha não cumpre as regras
    public boolean changePassword(String newPassword) {
        requireLoggedIn();

        if (!PasswordValidator.isValid(newPassword)) {
            return false;
        }

        currentUser.setPassword(newPassword);
        repository.save(currentUser);
        return true;
    }

    private void requireLoggedIn() {
        if (currentUser == null) {
            throw new IllegalStateException("No user logged in");
        }
    }
}