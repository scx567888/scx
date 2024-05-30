package cool.scx.common.scheduler;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 带有计数器的 Runnable 内部包含一个被调用次数的计数器
 *
 * @author scx567888
 * @version 1.11.8
 */
class CounterRunnable implements Runnable {

    /**
     * 已执行次数
     */
    final AtomicLong runCount = new AtomicLong(1);

    /**
     * 执行 scxHandler
     */
    final Consumer<ScheduleStatus> scxHandler;

    /**
     * ScheduledFuture 对象 用于手动取消
     */
    ScheduledFuture<?> scheduledFuture = null;

    public CounterRunnable(Consumer<ScheduleStatus> scxHandler) {
        this.scxHandler = scxHandler;
    }

    @Override
    public void run() {
        scxHandler.accept(new ScheduleStatus(runCount.getAndIncrement(), scheduledFuture));
    }

    public ScheduledFuture<?> schedule(TaskScheduler executor, Trigger trigger) {
        this.scheduledFuture = executor.schedule(this, trigger);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(TaskScheduler executor, Instant startTime, Duration delay) {
        this.scheduledFuture = executor.scheduleAtFixedRate(this, startTime, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(TaskScheduler executor, Duration delay) {
        this.scheduledFuture = executor.scheduleAtFixedRate(this, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(TaskScheduler executor, Instant startTime, Duration delay) {
        this.scheduledFuture = executor.scheduleWithFixedDelay(this, startTime, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(TaskScheduler executor, Duration delay) {
        this.scheduledFuture = executor.scheduleWithFixedDelay(this, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(ScheduledExecutorService taskScheduler, long initialDelay, long period, TimeUnit unit) {
        this.scheduledFuture = taskScheduler.scheduleAtFixedRate(this, initialDelay, period, unit);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(ScheduledExecutorService taskScheduler, long initialDelay, long delay, TimeUnit unit) {
        this.scheduledFuture = taskScheduler.scheduleWithFixedDelay(this, initialDelay, delay, unit);
        return this.scheduledFuture;
    }

}
