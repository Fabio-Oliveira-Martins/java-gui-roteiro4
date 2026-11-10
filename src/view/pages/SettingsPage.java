package view.pages;

import controller.AuthController;
import view.components.sections.Section;
import view.components.SettingRow;
import view.components.customDialogs.editDialog.EditPasswordDialog;
import view.components.customDialogs.editDialog.EditUsernameDialog;

import javax.swing.*;
import java.awt.Window;
import java.util.List;

public class SettingsPage extends Page {
    private final AuthController auth;

    public SettingsPage(AuthController auth) {
        this.auth = auth;
    }

    @Override
    protected List<Section> createSections() {
        Section profile = new Section("Profile");
        profile.setContent(new SettingRow(
                "Username",
                "Choose a unique username to identify your account",
                "Change Username",
                () -> new EditUsernameDialog(owner(), auth).open()
        ));

        Section security = new Section("Security");
        security.setContent(new SettingRow(
                "Password",
                "Set a unique password to protect your account",
                "Change Password",
                () -> new EditPasswordDialog(owner(), auth).open()
        ));

        return List.of(profile, security);
    }

    // Resolvido no clique, quando a página já está dentro da janela
    private Window owner() {
        return SwingUtilities.getWindowAncestor(this);
    }
}