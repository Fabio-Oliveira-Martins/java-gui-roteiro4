package view.components.customDialogs.statusDialog;

import java.awt.Window;

public class SuccessDialog extends StatusDialog {

    private static final String ICON = "check.png";
    private static final String DEFAULT_TITLE = "Success!";

    public SuccessDialog(
            Window owner,
            String title,
            String message
    ) {
        super(
                owner,
                ICON,
                title,
                message,
                "OK",
                null
        );
    }

    public static void display(Window owner, String message) {
        new SuccessDialog(
                owner,
                DEFAULT_TITLE,
                message
        ).open();
    }

    public static void display(
            Window owner,
            String title,
            String message
    ) {
        new SuccessDialog(
                owner,
                title,
                message
        ).open();
    }
}