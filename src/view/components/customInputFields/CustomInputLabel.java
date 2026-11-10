package view.components.customInputFields;

import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

import static utils.TextFormatter.capitalize;

public class CustomInputLabel extends JPanel implements Themeable {
    private static final LocalFont FONT = new LocalFont("Nunito", FontWeight.BOLD, 18f);
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JLabel inputLabel = new JLabel();

    public CustomInputLabel(String label, boolean required) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        gbc.weighty = 0;

        inputLabel.setText(capitalize(label));
        inputLabel.setFont(FONT.getFont());
        add(inputLabel, gbc);

        if (required) {
            JLabel symbol = new JLabel("*");
            symbol.setFont(FONT.getFont());
            symbol.setForeground(Color.RED);

            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(0, 6, 0, 0);
            add(symbol, gbc);
        }

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        inputLabel.setForeground(theme.text);
    }
}