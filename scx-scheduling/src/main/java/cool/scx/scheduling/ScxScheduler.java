package cool.scx.scheduling;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * ScxScheduler (此调度器被设计为是单例的 因为创建多实例是没有意义的)
 */
public class ScxScheduler implements ScheduledExecutorService {

    private static ScxScheduler INSTANCE;

    private final ScheduledExecutorService s;

    private ScxScheduler() {
        //todo JDK 暂时没有办法使用 由虚拟线程底层支持的调度器 这里这种做法只是一种 hack 并不完善
        this.s = new ScheduledThreadPoolExecutor(Integer.MAX_VALUE, new ScxThreadFactory());
    }

    public static ScxScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScxScheduler();
        }
        return INSTANCE;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return s.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return s.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return s.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return s.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override
    public void shutdown() {
        s.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return s.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return s.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return s.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return s.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return s.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return s.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return s.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return s.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return s.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return s.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return s.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void close() {
        s.close();
    }

    @Override
    public void execute(Runnable command) {
        s.execute(command);
    }

}
