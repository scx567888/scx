package cool.scx.scheduling;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 所有任务都在虚拟线程中运行的调度器 (注意: 因所有任务都在独立的虚拟线程中运行, 所以无法实现等待任务结束的功能)
 */
public class ScxScheduledExecutorService implements ScheduledExecutorService {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final String namePrefix;

    private final ScheduledExecutorService s;

    public ScxScheduledExecutorService() {
        this.s = new ScheduledThreadPoolExecutor(1);
        this.namePrefix = "scx-" + POOL_NUMBER.getAndIncrement() + "-virtual-thread-";
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return s.schedule(wrap(command), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return s.schedule(wrap(callable), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return s.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return s.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
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
        return s.submit(wrap(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return s.submit(wrap(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return s.submit(wrap(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return s.invokeAll(wrap(tasks));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return s.invokeAll(wrap(tasks), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return s.invokeAny(wrap(tasks));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return s.invokeAny(wrap(tasks), timeout, unit);
    }

    @Override
    public void close() {
        s.close();
    }

    @Override
    public void execute(Runnable command) {
        s.execute(wrap(command));
    }

    private <T> Collection<? extends Callable<T>> wrap(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::wrap).collect(Collectors.toList());
    }

    private <T> Callable<T> wrap(Callable<T> task) {
        return () -> {
            var futureTask = new FutureTask<>(task);
            Thread.ofVirtual().name(namePrefix, threadNumber.getAndIncrement()).start(futureTask);
            return futureTask.get();
        };
    }

    private Runnable wrap(Runnable command) {
        return () -> Thread.ofVirtual().name(namePrefix, threadNumber.getAndIncrement()).start(command);
    }

}
