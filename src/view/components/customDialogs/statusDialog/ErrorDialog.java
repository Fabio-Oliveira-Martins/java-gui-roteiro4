package view.components.customDialogs.statusDialog;

import java.awt.Window;

public class ErrorDialog extends StatusDialog {

    private static final String ICON = "error.png";
    private static final String DEFAULT_TITLE = "Invalid!";

    public ErrorDialog(
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
        new ErrorDialog(
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
        new ErrorDialog(
                owner,
                title,
                message
        ).open();
    }
}