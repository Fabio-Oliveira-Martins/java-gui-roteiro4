package view.components.customDialogs.statusDialog;

import java.awt.Window;

public class AlertDialog extends StatusDialog {

    private static final String ICON = "alert.png";
    private static final String DEFAULT_TITLE = "Alert!";

    private AlertDialog(
            Window owner,
            String title,
            String message,
            Runnable onConfirm
    ) {
        super(
                owner,
                ICON,
                title,
                message,
                "Confirm",
                "Cancel",
                onConfirm
        );
    }

    public static void confirm(
            Window owner,
            String title,
            String message,
            Runnable onConfirm
    ) {
        new AlertDialog(
                owner,
                title,
                message,
                onConfirm
        ).open();
    }
}