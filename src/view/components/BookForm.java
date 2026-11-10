package view.components;

import model.Author;
import model.Book;
import model.BookRepository;
import model.Category;
import persistence.DataAccessException;
import view.components.customButtons.SubmitButton;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.customDialogs.statusDialog.SuccessDialog;
import view.components.customInputFields.CategorySelector;
import view.components.customInputFields.CustomInputField;
import view.components.customInputFields.CustomTextAreaField;

import javax.swing.*;
import java.awt.*;

public class BookForm extends JPanel {
    private static final int ROW_GAP = 32;
    private static final int COLUMN_GAP = 16;

    private final BookRepository repository;

    private final CustomInputField authorField = new CustomInputField("Author", "String", true);
    private final CustomInputField publisherField = new CustomInputField("Publisher", "String", true);
    private final CustomInputField titleField = new CustomInputField("Title", "String", true);
    private final CustomTextAreaField descriptionField = new CustomTextAreaField("Description");
    private final CategorySelector categoryField = new CategorySelector();
    private final CustomInputField isbnField = new CustomInputField("ISBN (International Standard Book Number)", "ISBN", true);
    private final SubmitButton submitButton = new SubmitButton("Submit");

    public BookForm(BookRepository repository) {
        this.repository = repository;

        setOpaque(false);
        setLayout(new GridBagLayout());

        addRow(0, columns(authorField, publisherField), ROW_GAP);
        addRow(1, titleField, ROW_GAP);
        addRow(2, descriptionField, ROW_GAP);
        addRow(3, categoryField, ROW_GAP);
        addRow(4, isbnField, ROW_GAP);
        addRow(5, submitButton, 0);

        submitButton.addActionListener(e -> submit());
    }

    // Adiciona um componente ocupando toda a largura
    private void addRow(int row, JComponent component, int bottomGap) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, bottomGap, 0);
        add(component, gbc);
    }

    // Painel transparente com colunas de largura igual
    private JPanel columns(JComponent... components) {
        JPanel panel = new JPanel(new GridLayout(1, components.length, COLUMN_GAP, 0));
        panel.setOpaque(false);
        for (JComponent component : components) {
            panel.add(component);
        }
        return panel;
    }

    private boolean validateForm() {
        boolean valid = true;

        // &= (e não &&) para sinalizar todos os campos inválidos de uma vez
        valid &= authorField.validateInput();
        valid &= publisherField.validateInput();
        valid &= titleField.validateInput();
        valid &= descriptionField.validateInput();
        valid &= categoryField.validateInput();
        valid &= isbnField.validateInput();

        return valid;
    }

    private void submit() {
        Window owner = SwingUtilities.getWindowAncestor(this);

        if (!validateForm()) {
            ErrorDialog.display(owner, "Check if there is something wrong in your form");
            return;
        }

        Book book = new Book(
                (String) titleField.getValue(),
                new Author((String) authorField.getValue()),
                (String) publisherField.getValue(),
                Category.fromActionCommand(categoryField.getValue()),
                Long.parseLong(String.valueOf(isbnField.getValue())),
                descriptionField.getValue()
        );

        try {
            if (!repository.add(book)) {
                ErrorDialog.display(owner, "A book with ISBN " + book.getIsbn() + " already exists");
                return;
            }
        } catch (DataAccessException e) {
            ErrorDialog.display(owner, e.getMessage()); // mensagem genérica, sem detalhes do banco
            return;
        }

        clear();
        SuccessDialog.display(owner, "Your form was successfully submitted");
    }

    private void clear() {
        authorField.clear();
        publisherField.clear();
        titleField.clear();
        descriptionField.clear();
        categoryField.clear();
        isbnField.clear();
    }
}