package view.components.customInputFields;

import view.components.customInputFields.customInputs.CustomInput;
import view.components.customInputFields.customInputs.ISBNInput;
import view.components.customInputFields.customInputs.StringInput;

import javax.swing.*;
import java.awt.*;

public class CustomInputField extends JPanel {
    private static final Color BG_COLOR = new Color(0,0,0,0);
    public CustomInputLabel inputLabel;
    public CustomInput<?> input;

    GridBagConstraints gbc = new GridBagConstraints();

    public CustomInputField(String label, String type, boolean required) {

        setBackground(BG_COLOR);
        setOpaque(false);
        setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridx = 0;

        inputLabel = new CustomInputLabel(label, required);

        switch (type.toLowerCase()) {
            case "string": input = new StringInput(); break;
            case "isbn": input = new ISBNInput(); break;
            default: throw new IllegalArgumentException("Invalid input type: " + type);
        }

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(inputLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,0,0,0);
        add(input, gbc);
    }

    public boolean validateInput() {
        return input.validateInput();
    }

    public void clear() { input.clear(); }

    public Object getValue() {
        return input.getValue();
    }
}