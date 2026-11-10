package view.components.customButtons;

import utils.IconLoader;

import javax.swing.*;
import java.awt.*;

public class IconButton extends JButton {
    public IconButton(String iconName, int size) {
        super(IconLoader.load(iconName, size));
        setPreferredSize(new Dimension(size + 12, size + 12));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}