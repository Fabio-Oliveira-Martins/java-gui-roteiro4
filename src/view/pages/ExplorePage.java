package view.pages;

import model.Book;
import model.BookRepository;
import model.Category;
import view.components.sections.Section;
import view.components.componentsUtils.BackgroundTask;
import view.components.customDialogs.statusDialog.ErrorDialog;
import view.components.sections.BookRowSection;
import view.components.sections.BooksSection;
import view.components.sections.ExploreSection;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.util.List;

public class ExplorePage extends Page {
    private static final int SIMULATED_DELAY_MS = 800; // simula uma operação demorada

    private final BookRepository repository;

    public ExplorePage(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    protected List<Section> createSections() {
        ExploreSection explore = new ExploreSection();
        Section recent = BookRowSection.recentlyAdded(repository, this::render);
        Section suggestion = BookRowSection.suggestion(repository, this::render);
        BooksSection books = new BooksSection("Books", repository.findAll(), repository, this::render);

        // Com filtro ativo as seções de destaque somem; sem filtro, volta o acervo completo
        explore.setOnFilterChange(() -> {
            boolean active = explore.isFilterActive();
            recent.setVisible(!active);
            suggestion.setVisible(!active);
            if (!active) books.showBooks(repository.findAll());
            revalidate();
            repaint();
        });

        // Lupa, Enter ou categoria: busca fora da EDT
        explore.setOnSearch(() -> search(explore, books));

        return List.of(explore, recent, suggestion, books);
    }

    private void search(ExploreSection explore, BooksSection books) {
        if (!explore.isFilterActive()) return; // nada a filtrar: o acervo completo já está na tela

        String text = explore.getSearchText();
        Category category = explore.getSelectedCategory();

        explore.setSearching(true); // desabilita a ação durante a execução

        new BackgroundTask<List<Book>>("book search",
                () -> {
                    Thread.sleep(SIMULATED_DELAY_MS);              // operação demorada simulada
                    // TEMPORÁRIO, para testar a falha (descomente, rode e busque "fail"):
                    // if ("fail".equalsIgnoreCase(text)) throw new IllegalStateException("Simulated failure");
                    return repository.search(text, category);      // fora da EDT
                },
                found -> books.showBooks(explore.isFilterActive() ? found : repository.findAll()), // na EDT
                error -> ErrorDialog.display(owner(), "Unable to complete the search"),
                () -> explore.setSearching(false)                  // restaura em sucesso ou falha
        ).execute();
    }

    private Window owner() {
        return SwingUtilities.getWindowAncestor(this);
    }
}