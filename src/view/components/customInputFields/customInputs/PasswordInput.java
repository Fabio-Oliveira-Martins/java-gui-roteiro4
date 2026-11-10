package view.components.customInputFields.customInputs;

import utils.IconLoader;
import utils.PasswordValidator;
import view.themes.Theme;
import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class PasswordInput extends CustomInput<String> {
    // Versões escuras (tema claro) e claras (tema escuro) de cada ícone
    private static final String ICON_HIDDEN = "eye-off.png";
    private static final String ICON_HIDDEN_LIGHT = "eye-off-light.png";
    private static final String ICON_VISIBLE = "eye.png";
    private static final String ICON_VISIBLE_LIGHT = "eye-light.png";
    private static final int ICON_SIZE = 20;

    private final JPasswordField input = new JPasswordField();
    private final JToggleButton toggle = new JToggleButton();
    private final char hiddenEchoChar = input.getEchoChar();

    public PasswordInput() {
        setLayout(new BorderLayout(8, 0));

        input.setFont(getFont());
        input.setOpaque(false);
        input.setBorder(null);

        toggle.setPreferredSize(new Dimension(24, 24)); // mantém a área clicável mesmo sem ícone
        toggle.setOpaque(false);
        toggle.setContentAreaFilled(false);
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setFocusable(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.addActionListener(e -> input.setEchoChar(toggle.isSelected() ? (char) 0 : hiddenEchoChar));

        add(input, BorderLayout.CENTER);
        add(toggle, BorderLayout.EAST);

        registerFocusComponent(input);
        clearErrorOnEdit(input);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        input.setForeground(theme.text);
        input.setCaretColor(theme.text);

        // Tema escuro: ícones claros. Tema claro: ícones escuros.
        boolean dark = theme == Theme.DARK;
        toggle.setIcon(IconLoader.load(dark ? ICON_HIDDEN_LIGHT : ICON_HIDDEN, ICON_SIZE));
        toggle.setSelectedIcon(IconLoader.load(dark ? ICON_VISIBLE_LIGHT : ICON_VISIBLE, ICON_SIZE));
    }

    @Override
    public boolean validateInput() {
        if (!PasswordValidator.isValid(getValue())) {
            errorStyle();
            return false;
        }

        resetStyle();
        return true;
    }

    // Sem trim: espaços podem fazer parte da senha
    @Override
    public String getValue() {
        return new String(input.getPassword());
    }

    @Override
    public void clear() {
        input.setText("");
        toggle.setSelected(false);
        input.setEchoChar(hiddenEchoChar);
        resetStyle();
    }
}