import controller.AuthController;
import model.BookRepository;
import model.InMemoryUserRepository;
import model.User;
import model.UserRepository;
import persistence.jdbc.Database;
import persistence.jdbc.DatabaseConfig;
import view.LoginScreen;
import view.MainFrame;
import view.components.Header;
import view.components.NavigationBar;
import view.components.customDialogs.statusDialog.AlertDialog;
import view.pages.AddBookPage;
import view.pages.ExplorePage;
import view.pages.HomePage;
import view.pages.MyLibraryPage;
import view.pages.SettingsPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Configuração externa (variáveis de ambiente) e teste de conexão antes de abrir a janela
            BookRepository bookRepository;
            try {
                Database database = new Database(DatabaseConfig.fromEnvironment());
                database.check();
                bookRepository = new BookRepository(database);
            } catch (RuntimeException e) { // a mensagem não contém a senha
                JOptionPane.showMessageDialog(null, e.getMessage(), "Digital Library", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserRepository userRepository = new InMemoryUserRepository();
            userRepository.save(new User("Guest123", "Guest@123")); // usuário pré-definido
            AuthController auth = new AuthController(userRepository);

            Header header = new Header("Digital Library", "");
            auth.setOnUserChanged(user -> header.setUserName(user.getUsername()));

            NavigationBar navigationBar = new NavigationBar();
            MainFrame frame = new MainFrame(header, navigationBar, new LoginScreen(auth));

            // Mantenha os nomes de ícone que você já definiu
            navigationBar.addItem("Home", "home.png", "home-light.png",
                    () -> new HomePage(bookRepository));
            navigationBar.addItem("Explore", "compass.png", "compass-light.png",
                    () -> new ExplorePage(bookRepository));
            navigationBar.addItem("My Library", "bookmark.png", "bookmark-light.png",
                    () -> new MyLibraryPage(bookRepository));
            navigationBar.addItem("Add Book", "plus.png", "plus-light.png",
                    () -> new AddBookPage(bookRepository));
            navigationBar.addSeparator();
            navigationBar.addItem("Settings", "settings.png", "settings-light.png",
                    () -> new SettingsPage(auth));
            navigationBar.addAction("Logout", "logout.png", "logout-light.png",
                    () -> AlertDialog.confirm(
                            frame,
                            "Log Out",
                            "Are you sure that you want to log out?",
                            () -> {
                                auth.logout();
                                frame.showLogin();
                            }));

            frame.showLogin();
            frame.setVisible(true);
        });
    }
}