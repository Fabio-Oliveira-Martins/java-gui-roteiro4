package view.components.customDialogs.editDialog;

import view.components.customDialogs.CustomDialog;
import view.components.customDialogs.DialogTextArea;
import view.components.customDialogs.statusDialog.SuccessDialog;
import view.components.customInputFields.customInputs.CustomInput;
import view.themes.ThemeManager;
import view.typograph.FontWeight;
import view.typograph.LocalFont;

import javax.swing.*;
import java.awt.*;

public abstract class EditDialog extends CustomDialog {

    private static final String ICON = "edit.png";

    private static final Font MESSAGE_FONT =
            new LocalFont("Nunito", FontWeight.BOLD, 14f).getFont();

    private final CustomInput<String> input;

    protected EditDialog(
            Window owner,
            String title,
            String message,
            CustomInput<String> input
    ) {
        super(
                owner,
                ICON,
                title,
                createBody(message, input),
                "Confirm",
                "Cancel"
        );

        this.input = input;

        confirmButton.addActionListener(e -> {

            if (this.input.validateInput()) {
                onConfirm(this.input.getValue());
            } else {
                onInvalid();
            }

        });
    }

    protected abstract void onConfirm(String value);

    protected void onInvalid() {}

    protected void finish(String successMessage) {

        Window owner = getOwner();

        dispose();

        SuccessDialog.display(
                owner,
                successMessage
        );
    }

    private static JPanel createBody(
            String message,
            CustomInput<String> input
    ) {

        DialogTextArea messageArea = new DialogTextArea(
                message,
                CONTENT_WIDTH,
                MESSAGE_FONT,
                false
        );

        messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel body = new JPanel();

        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(messageArea);
        body.add(Box.createVerticalStrut(8));
        body.add(input);

        return body;
    }
}