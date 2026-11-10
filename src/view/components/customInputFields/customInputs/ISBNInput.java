package view.components.customInputFields.customInputs;

import view.themes.Theme;
import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class ISBNInput extends CustomInput<Long> {

    private final JTextField input = new JTextField();

    public ISBNInput() {
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
        Long value = getValue();
        int length = String.valueOf(value).length();

        if(value == null || length < 4) {
            errorStyle();
            return false;
        }

        boolean isInvalidLength = length != 13;

        String prefix = String.valueOf(value).substring(0,3);
        boolean isInvalidPrefix = !prefix.equals("978") && !prefix.equals("979");

        if (isInvalidLength || isInvalidPrefix) {
            errorStyle();
            return false;
        }

        resetStyle();
        return true;
    }

    // Converte direto do texto; null se vazio ou inválido
    @Override
    public Long getValue() {
        try {
            return Long.valueOf(input.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void clear() {
        input.setText("");
        resetStyle();
    }
}