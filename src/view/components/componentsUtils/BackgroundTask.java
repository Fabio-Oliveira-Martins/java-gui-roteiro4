package view.components.componentsUtils;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

// Executa "work" fora da EDT e entrega o resultado na EDT.
// onFinish roda sempre (sucesso ou falha): é onde a interface é restaurada.
public class BackgroundTask<T> extends SwingWorker<T, Void> {
    private final String name;
    private final Callable<T> work;
    private final Consumer<T> onSuccess;
    private final Consumer<Throwable> onFailure;
    private final Runnable onFinish;

    private volatile long startedAt;

    public BackgroundTask(String name, Callable<T> work, Consumer<T> onSuccess,
                          Consumer<Throwable> onFailure, Runnable onFinish) {
        this.name = name;
        this.work = work;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.onFinish = onFinish;
    }

    @Override
    protected T doInBackground() throws Exception {
        startedAt = System.nanoTime();
        System.out.printf("[%s] started | thread=%s | EDT=%b%n",
                name, Thread.currentThread().getName(), SwingUtilities.isEventDispatchThread());
        return work.call(); // fora da EDT
    }

    @Override
    protected void done() { // sempre na EDT
        long ms = (System.nanoTime() - startedAt) / 1_000_000;
        try {
            T result = get();
            log("ok", ms, result instanceof Collection<?> c ? c.size() + " item(s)" : String.valueOf(result));
            onSuccess.accept(result);
        } catch (ExecutionException e) {
            log("failed", ms, e.getCause().getClass().getSimpleName());
            onFailure.accept(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("interrupted", ms, "-");
            onFailure.accept(e);
        } finally {
            onFinish.run(); // restaura a interface
        }
    }

    private void log(String status, long ms, String detail) {
        System.out.printf("[%s] %s in %d ms | callback EDT=%b | %s%n",
                name, status, ms, SwingUtilities.isEventDispatchThread(), detail);
    }
}