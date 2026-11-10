package view.components.customInputFields.customInputs;

import view.themes.Theme;
import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class StringInput extends CustomInput<String> {

    private final JTextField input = new JTextField();

    public StringInput() {
        setLayout(new BorderLayout());

        input.setFont(getFont());
        input.setOpaque(false);
        input.setBorder(null);

        add(input, BorderLayout.CENTER);

        registerFocusComponent(input);
        clearErrorOnEdit(input);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        input.setForeground(theme.text);
        input.setCaretColor(theme.text);
    }

    @Override
    public boolean validateInput() {
        String value = getValue();

        if (value.isEmpty() || value.length() > 80) {
            errorStyle();
            return false;
        }

        resetStyle();
        return true;
    }

    @Override
    public String getValue() {
        return input.getText().trim();
    }

    @Override
    public void clear() {
        input.setText("");
        resetStyle();
    }
}