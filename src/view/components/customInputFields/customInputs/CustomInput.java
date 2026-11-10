package view.components.customInputFields.customInputs;

import view.components.componentsUtils.RoundedCorners;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.themes.Themeable;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class CustomInput<T> extends JPanel implements FocusListener, Themeable {

    private static final int PADDING_HORIZONTAL = 16;
    private static final int PADDING_VERTICAL = 8;
//    private static final Color BORDER_COLOR_FOCUS = Color.BLUE;
    private static final Color BORDER_COLOR_ERROR = Color.RED;
    private static final int RADIUS = 16;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.BOLD, 16f).getFont();

    private Color bgColor;
    private Color borderColor;
    private Color borderColorFocus;
    private boolean focused;
    private boolean hasError;

    public CustomInput() {
        setOpaque(false);
        setFont(FONT);
        applyBaseColors(ThemeManager.current());
    }

    private void applyBaseColors(Theme theme) {
        bgColor = theme.inputBackground;
        borderColor = theme.border;
        borderColorFocus = theme.accent;
        setForeground(theme.text);
        updateBorder();
    }

    @Override
    public void applyTheme(Theme theme) {
        applyBaseColors(theme);
    }

    private void updateBorder() {
        Color color = hasError ? BORDER_COLOR_ERROR : focused ? borderColorFocus : borderColor;
        RoundedCorners.apply(this, RADIUS, color, 2,
                PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL);
    }

    public void errorStyle() {
        hasError = true;
        updateBorder();
    }

    public void resetStyle() {
        hasError = false;
        updateBorder();
    }

    public boolean hasError() {
        return hasError;
    }

    protected void registerFocusComponent(JComponent component) {
        component.addFocusListener(this);
    }

    protected void clearErrorOnEdit(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void clear() { if (hasError) resetStyle(); }
            @Override public void insertUpdate(DocumentEvent e) { clear(); }
            @Override public void removeUpdate(DocumentEvent e) { clear(); }
            @Override public void changedUpdate(DocumentEvent e) { clear(); }
        });
    }

    @Override
    public void focusGained(FocusEvent e) {
        focused = true;
        updateBorder();
    }

    @Override
    public void focusLost(FocusEvent e) {
        focused = false;
        updateBorder();
    }

    @Override
    protected void paintComponent(Graphics g) {
        RoundedCorners.fillRoundedRectangle(g, bgColor, 0, 0, getWidth(), getHeight(), RADIUS);
        super.paintComponent(g);
    }

    public abstract boolean validateInput();
    public abstract T getValue();
    public abstract void clear();
}