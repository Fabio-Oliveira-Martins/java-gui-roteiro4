package view.components.customDialogs.editDialog;

import controller.AuthController;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.customInputFields.customInputs.PasswordInput;

import java.awt.Window;

public class EditPasswordDialog extends EditDialog {

    private final AuthController auth;

    public EditPasswordDialog(
            Window owner,
            AuthController auth
    ) {
        super(
                owner,
                "Edit Password",
                "New password:",
                new PasswordInput()
        );

        this.auth = auth;
    }

    @Override
    protected void onConfirm(String value) {

        if (!auth.changePassword(value)) {
            ErrorDialog.display(
                    this,
                    "Unable to update password"
            );

            return;
        }

        finish("Your password was successfully updated");
    }
}