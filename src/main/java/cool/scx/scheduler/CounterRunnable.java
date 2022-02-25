package cool.scx.scheduler;

import cool.scx.functional.ScxHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 带有计数器的 Runnable 内部包含一个被调用次数的计数器
 */
class CounterRunnable implements Runnable {

    /**
     * 已执行次数
     */
    final AtomicLong runCount = new AtomicLong(1);

    /**
     * 执行 scxHandler
     */
    final ScxHandler<ScheduleStatus> scxHandler;

    /**
     * ScheduledFuture 对象 用于手动取消
     */
    ScheduledFuture<?> scheduledFuture = null;

    public CounterRunnable(ScxHandler<ScheduleStatus> scxHandler) {
        this.scxHandler = scxHandler;
    }

    @Override
    public void run() {
        scxHandler.handle(new ScheduleStatus(runCount.getAndIncrement(), scheduledFuture));
    }

    public ScheduledFuture<?> schedule(TaskScheduler executor, Trigger trigger) {
        this.scheduledFuture = executor.schedule(this, trigger);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(TaskScheduler executor, Instant startTime, Duration delay) {
        this.scheduledFuture = executor.scheduleWithFixedDelay(this, startTime, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(TaskScheduler executor, Instant startTime, Duration delay) {
        this.scheduledFuture = executor.scheduleAtFixedRate(this, startTime, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(TaskScheduler executor, Duration delay) {
        this.scheduledFuture = executor.scheduleWithFixedDelay(this, delay);
        return this.scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(TaskScheduler executor, Duration delay) {
        this.scheduledFuture = executor.scheduleAtFixedRate(this, delay);
        return this.scheduledFuture;
    }

}