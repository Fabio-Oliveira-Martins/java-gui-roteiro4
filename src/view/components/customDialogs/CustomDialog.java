package view.components.customDialogs;

import utils.IconLoader;
import view.components.componentsUtils.RoundedCorners;
import view.components.customButtons.SecondaryButton;
import view.components.customButtons.SubmitButton;
import view.themes.Theme;
import view.themes.ThemeManager;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public abstract class CustomDialog extends JDialog {

    private static final int WIDTH = 400;
    private static final int RADIUS = 16;
    private static final int BORDER = 2;

    private static final int ICON_SIZE = 48;
    private static final int BUTTON_WIDTH = 112;
    private static final int BUTTON_HEIGHT = 36;
    private static final int FOOTER_HEIGHT = 72;

    private static final Font TITLE_FONT =
            new LocalFont("Nunito", FontWeight.BOLD, 20f).getFont();

    protected static final int CONTENT_WIDTH = 352;

    protected final SubmitButton confirmButton;
    protected final SecondaryButton cancelButton;

    protected CustomDialog(
            Window owner,
            String iconName,
            String title,
            JComponent body,
            String confirmButtonText,
            String cancelButtonText
    ) {
        super(owner, Dialog.ModalityType.APPLICATION_MODAL);

        Theme theme = ThemeManager.current();

        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== Conteúdo principal =====

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.setBorder(
                BorderFactory.createEmptyBorder(24, 24, 24, 24)
        );

        JLabel iconLabel = new JLabel(
                IconLoader.load(iconName, ICON_SIZE)
        );

        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(theme.text);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel bodyContainer = new JPanel(new GridBagLayout());

        bodyContainer.setOpaque(false);
        bodyContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        bodyContainer.setPreferredSize(new Dimension(CONTENT_WIDTH, body.getPreferredSize().height));
        bodyContainer.setMinimumSize(new Dimension(CONTENT_WIDTH, body.getPreferredSize().height));
        bodyContainer.setMaximumSize(new Dimension(CONTENT_WIDTH, body.getPreferredSize().height));

        bodyContainer.add(body);

        content.add(iconLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(bodyContainer);

        // ===== Botões =====

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 6, 0, 6);

        cancelButton = hasText(cancelButtonText)
                ? SecondaryButton.filled(cancelButtonText, null)
                : null;

        confirmButton = hasText(confirmButtonText)
                ? new SubmitButton(confirmButtonText, null)
                : null;

        if (cancelButton != null) {
            configureButton(cancelButton);
            cancelButton.addActionListener(e -> dispose());

            buttons.add(cancelButton, gbc);
            gbc.gridx++;
        }

        if (confirmButton != null) {
            configureButton(confirmButton);

            buttons.add(confirmButton, gbc);
            getRootPane().setDefaultButton(confirmButton);
        }

        // ===== Rodapé =====

        JPanel footer = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {
                int radius = RADIUS - BORDER;

                RoundedCorners.fillRoundedRectangle(
                        g,
                        theme.background,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        0,
                        0,
                        radius,
                        radius
                );
            }
        };

        footer.setOpaque(false);
        footer.setPreferredSize(
                new Dimension(WIDTH, FOOTER_HEIGHT)
        );

        footer.setMinimumSize(
                new Dimension(0, FOOTER_HEIGHT)
        );

        footer.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, FOOTER_HEIGHT)
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(0, 16, 0, 16)
        );

        footer.add(buttons, BorderLayout.CENTER);

        // ===== Janela =====

        JPanel root = new JPanel(new BorderLayout()) {

            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();

                return new Dimension(WIDTH, size.height);
            }

            @Override
            protected void paintComponent(Graphics g) {
                RoundedCorners.fillRoundedRectangle(
                        g,
                        theme.surface,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        RADIUS
                );
            }
        };

        root.setOpaque(false);

        RoundedCorners.apply(
                root,
                RADIUS,
                theme.border,
                BORDER,
                0
        );

        root.add(content, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);

        // ===== Tecla ESC =====

        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private static boolean hasText(String text) {
        return text != null && !text.isBlank();
    }

    private static void configureButton(JButton button) {
        Dimension size = new Dimension(
                BUTTON_WIDTH,
                BUTTON_HEIGHT
        );

        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }

    public final void open() {
        pack();

        // Largura fixa e altura calculada pelo conteúdo.
        setSize(WIDTH, getHeight());

        setShape(
                new RoundRectangle2D.Double(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        RADIUS * 2,
                        RADIUS * 2
                )
        );

        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
}
