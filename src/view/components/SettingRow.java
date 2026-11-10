package view.components;

import view.components.customButtons.SecondaryButton;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public class SettingRow extends JPanel implements Themeable {
    private static final Font TITLE_FONT = new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();
    private static final Font DESCRIPTION_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 11f).getFont();

    private final JLabel titleLabel = new JLabel();
    private final JLabel descriptionLabel = new JLabel();

    public SettingRow(String title, String description, String buttonText, Runnable onClick) {
        setOpaque(false);
        setLayout(new BorderLayout(16, 0));

        titleLabel.setText(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionLabel.setText(description);
        descriptionLabel.setFont(DESCRIPTION_FONT);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(descriptionLabel);

        SecondaryButton button = SecondaryButton.outlined(buttonText);
        button.addActionListener(e -> onClick.run());

        JPanel buttonWrapper = new JPanel(new GridBagLayout()); // centraliza na vertical
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(button);

        add(text, BorderLayout.CENTER);
        add(buttonWrapper, BorderLayout.EAST);

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        titleLabel.setForeground(theme.text);
        descriptionLabel.setForeground(theme.textMuted);
    }
}