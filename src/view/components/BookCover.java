package view.components;

import view.components.componentsUtils.RoundedCorners;

import javax.swing.*;
import java.awt.*;

public class BookCover extends JComponent {
    private static final int RADIUS = 12;
    private final Color color;

    public BookCover(Color color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // futuramente: desenhar aqui a imagem de capa do livro
        RoundedCorners.fillRoundedRectangle(g, color, 0, 0, getWidth(), getHeight(), RADIUS);
    }
}