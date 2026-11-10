package view;

import view.components.Header;
import view.components.NavigationBar;
import view.components.PageContainer;
import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String APP = "app";

    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(cards);
    private final PageContainer pageContainer = new PageContainer();
    private final NavigationBar navigationBar;

    public MainFrame(Header header, NavigationBar navigationBar, LoginScreen loginScreen) {
        super("Digital Library");
        this.navigationBar = navigationBar;
        setMinimumSize(new Dimension(900, 700));

        navigationBar.setOnNavigate(pageContainer::showPage);
        loginScreen.setOnSuccess(this::showApp);

        JPanel app = new JPanel(new BorderLayout());
        app.add(header, BorderLayout.NORTH);
        app.add(navigationBar, BorderLayout.WEST);
        app.add(pageContainer, BorderLayout.CENTER);

        screens.add(loginScreen, LOGIN);
        screens.add(app, APP);
        add(screens);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ThemeManager.install(getContentPane());
    }

    public void showLogin() {
        cards.show(screens, LOGIN);
    }

    public void showApp() {
        cards.show(screens, APP);
        navigationBar.selectFirst(); // abre a Home
    }
}