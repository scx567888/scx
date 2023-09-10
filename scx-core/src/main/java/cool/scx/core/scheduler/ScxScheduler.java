package cool.scx.core.scheduler;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 针对 spring 的 ${@link org.springframework.scheduling.TaskScheduler}  进行一些简单的封装
 * <br>
 * 以便可以实现一些简单的任务调度
 * todo 一些方法 不需要提供 ScheduleStatus 方法
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxScheduler implements ScheduledExecutorService, TaskScheduler {

    private final ScheduledExecutorService scheduledExecutorService;

    private final TaskScheduler taskScheduler;

    public ScxScheduler(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.taskScheduler = new ConcurrentTaskScheduler(this.scheduledExecutorService);
    }


    //************* TaskScheduler *********

    @Override
    @Nullable
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return taskScheduler.schedule(task, trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Instant startTime) {
        return taskScheduler.schedule(task, startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
        return taskScheduler.scheduleAtFixedRate(task, startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
        return taskScheduler.scheduleAtFixedRate(task, period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Instant startTime, Duration delay) {
        return taskScheduler.scheduleWithFixedDelay(task, startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Duration delay) {
        return taskScheduler.scheduleWithFixedDelay(task, delay);
    }

    //*************** ScheduledExecutorService *********************

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(runnable, delay, unit);
    }

    @Override
    public <R> ScheduledFuture<R> schedule(Callable<R> scxHandlerVR, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(scxHandlerVR, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }


    //************************ ExecutorService **********************

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return scheduledExecutorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return scheduledExecutorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return scheduledExecutorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return scheduledExecutorService.awaitTermination(timeout, unit);
    }

    @Override
    public <R> Future<R> submit(Callable<R> task) {
        return scheduledExecutorService.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return scheduledExecutorService.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return scheduledExecutorService.submit(runnable);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return scheduledExecutorService.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return scheduledExecutorService.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return scheduledExecutorService.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return scheduledExecutorService.invokeAny(tasks, timeout, unit);
    }

    //*************** Executor ********************

    @Override
    public void execute(Runnable command) {
        scheduledExecutorService.execute(command);
    }


    //****************** ScxScheduler For TaskScheduler *********************

    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, Trigger trigger) {
        return new CounterRunnable(scxHandler).schedule(taskScheduler, trigger);
    }

    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, Instant startTime) {
        return new CounterRunnable(scxHandler).schedule(taskScheduler, startTime);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, Instant startTime, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleAtFixedRate(taskScheduler, startTime, delay);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleAtFixedRate(taskScheduler, delay);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, Instant startTime, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleWithFixedDelay(taskScheduler, startTime, delay);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleWithFixedDelay(taskScheduler, delay);
    }

    //****************** ScxScheduler For ScheduledExecutorService ***************


    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, long delay, TimeUnit unit) {
        return new CounterRunnable(scxHandler).schedule(scheduledExecutorService, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, long initialDelay, long period, TimeUnit unit) {
        return new CounterRunnable(scxHandler).scheduleAtFixedRate(scheduledExecutorService, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, long initialDelay, long delay, TimeUnit unit) {
        return new CounterRunnable(scxHandler).scheduleWithFixedDelay(scheduledExecutorService, initialDelay, delay, unit);
    }

    //***************  FixedRunCountRunnable ******************

    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, Trigger trigger, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).schedule(taskScheduler, trigger);
    }

    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, Instant startTime, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).schedule(taskScheduler, startTime);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, Instant startTime, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleAtFixedRate(taskScheduler, startTime, delay);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleAtFixedRate(taskScheduler, delay);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, Instant startTime, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleWithFixedDelay(taskScheduler, startTime, delay);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleWithFixedDelay(taskScheduler, delay);
    }

    public ScheduledFuture<?> schedule(Consumer<ScheduleStatus> scxHandler, long delay, TimeUnit unit, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).schedule(scheduledExecutorService, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Consumer<ScheduleStatus> scxHandler, long initialDelay, long period, TimeUnit unit, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleAtFixedRate(scheduledExecutorService, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Consumer<ScheduleStatus> scxHandler, long initialDelay, long delay, TimeUnit unit, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleWithFixedDelay(scheduledExecutorService, initialDelay, delay, unit);
    }

}
