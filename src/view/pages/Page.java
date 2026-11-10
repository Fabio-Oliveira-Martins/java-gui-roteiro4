package view.pages;

import persistence.DataAccessException;
import view.components.MutedLabel;
import view.components.sections.Section;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Page extends JPanel implements Scrollable {
    private static final int GAP = 16;

    protected Page() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
    }

    // Template method: o roteiro é igual para todas as páginas
    public final void render() {
        List<Section> sections;
        try {
            sections = createSections();
        } catch (DataAccessException e) {
            Section error = new Section("Something went wrong");
            error.setContent(new MutedLabel(e.getMessage()));
            sections = List.of(error);
        }

        removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, GAP, 0);

        for (Section section : sections) {
            add(section, gbc);
        }

        // Preenchimento: absorve a sobra e mantém as seções no topo
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, gbc);

        onRendered();
        revalidate();
        repaint();
    }

    // Passo obrigatório: cada página diz quais seções tem
    protected abstract List<Section> createSections();

    // Gancho opcional
    protected void onRendered() {}

    // ===== Scrollable: largura segue o viewport, altura é a do conteúdo =====
    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 16; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
        return o == SwingConstants.VERTICAL ? r.height : r.width;
    }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}