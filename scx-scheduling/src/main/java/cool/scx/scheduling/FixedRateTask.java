package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FixedRateTask implements ScheduleTask {

    protected Instant startTime;
    protected Duration delay;
    protected Consumer<ScheduleStatus> task;
    protected ScheduledExecutorService executor;
    protected final FixedRateStatus status;

    public FixedRateTask() {
        this.startTime = null;
        this.delay = null;
        this.task = null;
        this.executor = null;
        this.status = new FixedRateStatus();
    }

    public FixedRateTask startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public FixedRateTask delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public FixedRateTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public FixedRateTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    public Instant startTime() {
        return startTime;
    }

    public Duration delay() {
        return delay;
    }

    public ScheduledExecutorService executor() {
        return executor;
    }

    public Consumer<ScheduleStatus> task() {
        return task;
    }

    protected void run() {
        try {
            task.accept(this.status);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public FixedRateStatus start() {
        if (this.delay == null) {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        var initialDelay = this.startTime != null ? between(this.startTime, now()).toNanos() : 0;
        var delay = this.delay.toNanos();
        var scheduledFuture = executor.scheduleAtFixedRate(this::run, initialDelay, delay, NANOSECONDS);
        return this.status.setScheduledFuture(scheduledFuture);
    }

}
