package view.components.customInputFields;

import view.components.customInputFields.customInputs.CustomTextArea;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class CustomTextAreaField extends JPanel implements Themeable {

    private static final int MAX_CHARS = 1200;
    private static final Font COUNTER_FONT = new LocalFont("Nunito", FontWeight.REGULAR, 14f).getFont();
    private static final Color COUNTER_COLOR_ERROR = Color.RED;

    private final CustomInputLabel inputLabel;
    private final CustomTextArea textArea;
    private final JLabel counterLabel;

    public CustomTextAreaField(String label) {
        setOpaque(false);
        setLayout(new BorderLayout());

        inputLabel = new CustomInputLabel(label, false);
        textArea = new CustomTextArea();
        counterLabel = new JLabel();

        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelWrapper.setOpaque(false);
        labelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        labelWrapper.add(inputLabel);

        counterLabel.setFont(COUNTER_FONT);
        counterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        counterLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 4));

        add(labelWrapper, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
        add(counterLabel, BorderLayout.SOUTH);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { refreshCounter(); }
            @Override public void removeUpdate(DocumentEvent e) { refreshCounter(); }
            @Override public void changedUpdate(DocumentEvent e) { refreshCounter(); }
        });

        refreshCounter();
    }

    private boolean exceedsLimit() {
        return textArea.getLength() > MAX_CHARS;
    }

    private void refreshCounter() {
        boolean exceeded = exceedsLimit();

        counterLabel.setText(textArea.getLength() + " / " + MAX_CHARS);
        counterLabel.setForeground(exceeded ? COUNTER_COLOR_ERROR : ThemeManager.current().textMuted);

        if (exceeded && !textArea.hasError()) {
            textArea.errorStyle();
        } else if (!exceeded && textArea.hasError()) {
            textArea.resetStyle();
        }
    }

    @Override
    public void applyTheme(Theme theme) {
        refreshCounter();
    }

    public boolean validateInput() {
        return !exceedsLimit();
    }

    public String getValue() {
        return textArea.getValue();
    }

    public void clear() {
        textArea.clear();
    }
}