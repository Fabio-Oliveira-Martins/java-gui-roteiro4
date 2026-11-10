package view.components.customDialogs.statusDialog;

import view.components.customDialogs.CustomDialog;
import view.components.customDialogs.DialogTextArea;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import java.awt.*;

public abstract class StatusDialog extends CustomDialog {

    private static final Font MESSAGE_FONT =
            new LocalFont("Nunito", FontWeight.REGULAR, 14f).getFont();

    protected StatusDialog(
            Window owner,
            String iconName,
            String title,
            String message,
            String confirmText,
            String cancelText
    ) {
        this(
                owner,
                iconName,
                title,
                message,
                confirmText,
                cancelText,
                null
        );
    }

    protected StatusDialog(
            Window owner,
            String iconName,
            String title,
            String message,
            String confirmText,
            String cancelText,
            Runnable onConfirm
    ) {
        super(
                owner,
                iconName,
                title,
                createMessage(message),
                confirmText,
                cancelText
        );

        if (confirmButton != null) {
            confirmButton.addActionListener(e -> {
                dispose();

                if (onConfirm != null) {
                    onConfirm.run();
                }
            });
        }
    }

    private static DialogTextArea createMessage(String message) {

        return new DialogTextArea(
                message,
                CONTENT_WIDTH,
                MESSAGE_FONT,
                true
        );
    }
}

