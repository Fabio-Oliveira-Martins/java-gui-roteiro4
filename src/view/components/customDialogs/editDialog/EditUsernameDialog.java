package view.components.customDialogs.editDialog;

import controller.AuthController;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.customInputFields.customInputs.StringInput;

import java.awt.Window;

public class EditUsernameDialog extends EditDialog {

    private final AuthController auth;

    public EditUsernameDialog(
            Window owner,
            AuthController auth
    ) {
        super(
                owner,
                "Edit Username",
                "New username:",
                new StringInput()
        );

        this.auth = auth;
    }

    @Override
    protected void onConfirm(String value) {

        if (!auth.changeUsername(value)) {
            ErrorDialog.display(
                    this,
                    "This username is already taken"
            );

            return;
        }

        finish("Your username was successfully updated");
    }
}