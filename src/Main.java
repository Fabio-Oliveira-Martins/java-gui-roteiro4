import controller.AuthController;
import model.*;
import view.LoginScreen;
import view.MainFrame;
import view.components.Header;
import view.components.NavigationBar;
import view.components.customDialogs.statusDialog.AlertDialog;
import view.pages.*;

import javax.swing.*;

public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            BookRepository bookRepository = new BookRepository();

            // TEMPORÁRIO: livros de exemplo
            for (int i = 1; i <= 14; i++) {
                bookRepository.add(new Book(
                        "Book Title " + i,
                        bookRepository.resolveAuthor("Author " + (i % 5 + 1)),
                        "Publisher " + i,
                        Category.values()[i % Category.values().length],
                        9780000000000L + i,
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(i % 3 + 1)));
            }

            UserRepository userRepository = new InMemoryUserRepository();
            userRepository.save(new User("Guest123", "Guest@123")); // usuário pré-definido
            AuthController auth = new AuthController(userRepository);

            Header header = new Header("Digital Library", "");
            auth.setOnUserChanged(user -> header.setUserName(user.getUsername()));

            NavigationBar navigationBar = new NavigationBar();
            MainFrame frame = new MainFrame(header, navigationBar, new LoginScreen(auth));

            navigationBar.addItem("Home", "home.png", "home-light.png", () -> new HomePage(bookRepository));
            navigationBar.addItem("Explore", "compass.png", "compass-light.png", () -> new ExplorePage(bookRepository));
            navigationBar.addItem("My Library", "bookmark.png", "bookmark-light.png", () -> new MyLibraryPage(bookRepository));
            navigationBar.addItem("Add Book", "plus.png", "plus-light.png", () -> new AddBookPage(bookRepository));
            navigationBar.addSeparator();
            navigationBar.addItem(
                    "Settings",
                    "settings.png",
                    "settings-light.png",
                    () -> new SettingsPage(auth)
            );

            navigationBar.addAction(
                    "Logout",
                    "logout.png",
                    "logout-light.png",
                    () -> AlertDialog.confirm(
                            frame,
                            "Log Out",
                            "Are you sure that you want to log out?",
                            () -> {
                                auth.logout();
                                frame.showLogin();
                            }
                    )
            );
            frame.showLogin();
            frame.setVisible(true);
        });
    }
}