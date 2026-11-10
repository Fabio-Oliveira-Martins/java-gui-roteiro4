package view.components.customInputFields;

import view.components.customInputFields.customInputs.CustomInputRadio;

import javax.swing.*;
import java.awt.*;

public class CategorySelector extends JPanel {

    public ButtonGroup group = new ButtonGroup();

    public CategorySelector() {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Label alinhado à esquerda
        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelWrapper.setOpaque(false);
        labelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        labelWrapper.add(new CustomInputLabel("Category", true));

        // 2 linhas x 3 colunas iguais
        JPanel options = new JPanel(new GridLayout(2, 3, 16, 8));
        options.setOpaque(false);

        String[] labels = {
                "Fiction", "Non-Fiction", "Children",
                "Academic", "Poetry & Drama", "Reference"
        };

        for (String label : labels) {
            CustomInputRadio radio = new CustomInputRadio(label);
            group.add(radio);
            options.add(radio);
        }

        add(labelWrapper, BorderLayout.NORTH);
        add(options, BorderLayout.CENTER);
    }

    // Campo obrigatório: precisa ter uma opção marcada
    public boolean validateInput() {
        return group.getSelection() != null;
    }

    // actionCommand da opção marcada (ex.: "fiction") ou null
    public String getValue() {
        ButtonModel selected = group.getSelection();
        return selected == null ? null : selected.getActionCommand();
    }

    public void clear() {
        group.clearSelection();
    }
}