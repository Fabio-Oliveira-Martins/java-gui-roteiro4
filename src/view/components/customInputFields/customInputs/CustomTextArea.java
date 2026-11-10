package view.components.customInputFields.customInputs;

import view.components.componentsUtils.CustomScrollBarUI;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTextArea extends CustomInput<String> {

    private static final int HEIGHT = 200;
    private static final Font FONT = new LocalFont("Nunito", FontWeight.REGULAR, 16f).getFont();

    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(
            textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
    );

    public CustomTextArea() {
        setLayout(new BorderLayout());

        textArea.setFont(FONT);
        textArea.setOpaque(false);
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        registerFocusComponent(textArea);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textArea.requestFocusInWindow();
            }
        });

        applyTheme(ThemeManager.current());
    }

    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        textArea.setForeground(theme.text);
        textArea.setCaretColor(theme.text);
        scrollPane.getViewport().setBackground(theme.inputBackground);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(
                new Color(theme.text.getRed(), theme.text.getGreen(), theme.text.getBlue(), 70),
                new Color(theme.text.getRed(), theme.text.getGreen(), theme.text.getBlue(), 120),
                null, 8, 4
        ));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = HEIGHT;
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, HEIGHT);
    }

    public Document getDocument() {
        return textArea.getDocument();
    }

    public int getLength() {
        return textArea.getDocument().getLength();
    }

    @Override
    public void errorStyle() {
        super.errorStyle();
    }

    @Override
    public void resetStyle() {
        super.resetStyle();
    }

    @Override
    public boolean validateInput() {
        return true;
    }

    @Override
    public String getValue() {
        return textArea.getText().trim();
    }

    @Override
    public void clear() {
        textArea.setText("");
    }
}