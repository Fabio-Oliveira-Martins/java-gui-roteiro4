package view.themes;

import java.awt.Color;

public enum Theme {
    LIGHT(
            new Color(226, 230, 235), // background
            new Color(255,255,255),   // surface (cards, header)
            new Color(248, 249, 252), // sidebar
            new Color(0,0,0),         // text
            new Color(0,0,0),         // subtitle
            new Color(110, 110, 120), // textMuted
            new Color(86, 52, 142),   // accent
            new Color(120, 84, 180),  // accentHover
            new Color(255,255,255),   // accentText
            new Color(232, 226, 243), // hover
            new Color(0,0,0),         // border
            new Color(255,255,255)    // inputBackground
    ),
    DARK(
            new Color(18, 18, 24),
            new Color(30, 30, 40),
            new Color(24, 24, 32),
            new Color(240, 240, 245),
            new Color(0,0,0),
            new Color(160, 160, 175),
            new Color(130, 90, 200),
            new Color(155, 118, 224),
            new Color(255,255,255),
            new Color(48, 42, 68),
            new Color(200, 200, 210),
            new Color(40, 40, 52)
    );

    public final Color background;
    public final Color surface;
    public final Color sidebar;
    public final Color text;
    public final Color subtitle;
    public final Color textMuted;
    public final Color accent;
    public final Color accentHover;
    public final Color accentText;
    public final Color hover;
    public final Color border;
    public final Color inputBackground;

    Theme(
            Color background, Color surface, Color sidebar, Color text,
            Color subtitle, Color textMuted, Color accent,
            Color accentHover, Color accentText, Color hover, Color border, Color inputBackground
    ) {
        this.background = background;
        this.surface = surface;
        this.sidebar = sidebar;
        this.text = text;
        this.subtitle = subtitle;
        this.textMuted = textMuted;
        this.accent = accent;
        this.accentHover = accentHover;
        this.accentText = accentText;
        this.hover = hover;
        this.border = border;
        this.inputBackground = inputBackground;
    }
}