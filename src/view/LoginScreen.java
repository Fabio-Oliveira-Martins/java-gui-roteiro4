package view;

import controller.AuthController;
import utils.PasswordValidator;
import view.components.customButtons.SubmitButton;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.customInputFields.CustomInputLabel;
import view.components.customInputFields.customInputs.PasswordInput;
import view.components.customInputFields.customInputs.StringInput;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel implements Themeable {
    private static final int FORM_WIDTH = 360;
    private static final int GAP = 24;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.BOLD, 32f).getFont();

    private final AuthController auth;
    private final JLabel title;
    private final StringInput usernameInput = new StringInput();
    private final PasswordInput passwordInput = new PasswordInput();
    private final SubmitButton submitButton = new SubmitButton("Submit");
    private Runnable onSuccess = () -> {};

    public LoginScreen(AuthController auth) {
        this.auth = auth;
        setLayout(new GridBagLayout()); // centraliza o formulário

        title = new JLabel("Login");
        title.setFont(FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        submitButton.setPreferredSize(new Dimension(0, 50));
        submitButton.addActionListener(e -> submit());

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, GAP, 0);

        gbc.gridy = 0; form.add(title, gbc);
        gbc.gridy = 1; form.add(labeled("Username", usernameInput), gbc);
        gbc.gridy = 2; form.add(labeled("Password", passwordInput), gbc);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0); form.add(submitButton, gbc);

        form.setPreferredSize(new Dimension(FORM_WIDTH, form.getPreferredSize().height));
        add(form);

        applyTheme(ThemeManager.current());
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private JPanel labeled(String text, JComponent input) {
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(new CustomInputLabel(text, true));

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(left, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        return panel;
    }

    private void submit() {
        Window owner = SwingUtilities.getWindowAncestor(this);

        // & (e não &&) para sinalizar os dois campos de uma vez
        boolean valid = usernameInput.validateInput() & passwordInput.validateInput();
        if (!valid) {
            ErrorDialog.display(owner, "The values entered are incorrect", PasswordValidator.RULES);
            return;
        }

        switch (auth.login(usernameInput.getValue(), passwordInput.getValue())) {
            case USER_NOT_FOUND -> {
                usernameInput.errorStyle();
                ErrorDialog.display(owner, "This user does not exist");
            }
            case WRONG_PASSWORD -> {
                passwordInput.errorStyle();
                ErrorDialog.display(owner, "Incorrect password");
            }
            case SUCCESS -> {
                usernameInput.clear();
                passwordInput.clear();
                onSuccess.run();
            }
        }
    }

    @Override
    public void applyTheme(Theme theme) {
        title.setForeground(theme.text);
        setBackground(theme.surface);
    }
}