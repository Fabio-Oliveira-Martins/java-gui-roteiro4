package view.components.customDialogs;

import view.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class DialogTextArea extends JTextArea {

    public DialogTextArea(
            String text,
            int width,
            Font font,
            boolean centered
    ) {
        setFont(font);
        setForeground(ThemeManager.current().text);

        // Remove a aparência padrão do JTextArea.
        setOpaque(false);
        setEditable(false);
        setFocusable(false);
        setBorder(null);
        setMargin(new Insets(0, 0, 0, 0));

        // A quebra de linha será controlada manualmente.
        setLineWrap(false);
        setWrapStyleWord(false);

        if (centered) {
            setCenteredText(text, width);
        } else {
            setLeftAlignedText(text, width);
        }
    }

    public void setCenteredText(String text, int width) {
        setFormattedText(text, width, true);
    }

    public void setLeftAlignedText(String text, int width) {
        setFormattedText(text, width, false);
    }

    private void setFormattedText(
            String text,
            int width,
            boolean centered
    ) {
        FontMetrics metrics = getFontMetrics(getFont());

        // Pequena margem para evitar ultrapassar os limites.
        int availableWidth = width - 8;

        StringBuilder formatted = new StringBuilder();

        String[] paragraphs = (text == null ? "" : text)
                .split("\\R", -1);

        for (String paragraph : paragraphs) {

            if (paragraph.isBlank()) {
                formatted.append("\n");
                continue;
            }

            StringBuilder line = new StringBuilder();

            String[] words = paragraph.trim().split("\\s+");

            for (String word : words) {

                String candidate = line.isEmpty()
                        ? word
                        : line + " " + word;

                if (metrics.stringWidth(candidate) <= availableWidth) {

                    if (!line.isEmpty()) {
                        line.append(" ");
                    }

                    line.append(word);

                } else {

                    if (!line.isEmpty()) {
                        appendLine(formatted, line.toString(), metrics,
                                availableWidth, centered);

                        line.setLength(0);
                    }

                    line.append(word);
                }
            }

            if (!line.isEmpty()) {
                appendLine(formatted, line.toString(), metrics,
                        availableWidth, centered);
            }
        }

        setText(formatted.toString());

        setCaretPosition(0);

        // Calcula a altura necessária.
        int lineCount = Math.max(1, getLineCount());

        int height = metrics.getHeight() * lineCount;

        Dimension size = new Dimension(width, height);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    private void appendLine(
            StringBuilder formatted,
            String line,
            FontMetrics metrics,
            int availableWidth,
            boolean centered
    ) {
        if (centered) {

            int remainingWidth =
                    availableWidth - metrics.stringWidth(line);

            int spaceWidth = Math.max(1, metrics.charWidth(' '));

            int spaces = Math.max(
                    0,
                    remainingWidth / (2 * spaceWidth)
            );

            formatted.append(" ".repeat(spaces));
        }

        formatted.append(line).append("\n");
    }
}
