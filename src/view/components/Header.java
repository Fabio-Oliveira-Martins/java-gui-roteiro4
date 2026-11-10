package view.components;

import utils.IconLoader;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel implements Themeable {
    private static final Font TITLE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 32f).getFont();
    private static final Font USER_FONT = new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();

    private static final int ICON_SIZE = 24;
    private static final int PROFILE_SIZE = 32;

    private final JLabel titleLabel = new JLabel();
    private final ThemeToggle themeToggle = new ThemeToggle();

    private final JLabel userIcon = new JLabel();
    private final JLabel userLabel = new JLabel();

    private final JPanel userContainer = new JPanel(new GridBagLayout());
    private final JPanel actions = new JPanel(new GridBagLayout());

    public Header(String title, String userName) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));

        titleLabel.setText(title);
        titleLabel.setFont(TITLE_FONT);

        // Utilizando a classe IconLoader passando apenas o nome do arquivo
        themeToggle.setIcon(IconLoader.load("sun.png", ICON_SIZE));
        themeToggle.setSelectedIcon(IconLoader.load("moon.png", ICON_SIZE));
        userIcon.setIcon(IconLoader.load("user.png", PROFILE_SIZE));

        userLabel.setText(userName);
        userLabel.setFont(USER_FONT);

        userContainer.setOpaque(false);
        GridBagConstraints gbcUser = new GridBagConstraints();
        gbcUser.anchor = GridBagConstraints.CENTER;
        gbcUser.insets = new Insets(0, 0, 0, 8);
        userContainer.add(userIcon, gbcUser);

        gbcUser.gridx = 1;
        gbcUser.insets = new Insets(0, 0, 0, 0);
        userContainer.add(userLabel, gbcUser);

        actions.setOpaque(false);
        GridBagConstraints gbcActions = new GridBagConstraints();
        gbcActions.anchor = GridBagConstraints.CENTER;

        gbcActions.gridx = 0;
        gbcActions.insets = new Insets(0, 0, 0, 16);
        actions.add(themeToggle, gbcActions);

        gbcActions.gridx = 1;
        gbcActions.insets = new Insets(0, 0, 0, 0);
        actions.add(userContainer, gbcActions);

        add(titleLabel, BorderLayout.WEST);
        add(actions, BorderLayout.EAST);

        applyTheme(ThemeManager.current());
    }

    public void setUserName(String userName) {
        userLabel.setText(userName);
    }

    @Override
    public void applyTheme(Theme theme) {
        setBackground(theme.surface);
        titleLabel.setForeground(theme.text);
        userLabel.setForeground(theme.text);
    }
}