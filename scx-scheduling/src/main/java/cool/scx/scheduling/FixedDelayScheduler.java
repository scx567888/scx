package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FixedDelayScheduler implements ScxScheduler {

    private Instant startTime;
    private Duration delay;
    private Consumer<ScxScheduleStatus> task;
    private ScheduledExecutorService executor;
    private AtomicLong runCount;

    public FixedDelayScheduler() {
        this.startTime = null;
        this.delay = null;
        this.task = null;
        this.executor = null;
        this.runCount = new AtomicLong(0);
    }

    public FixedDelayScheduler startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public FixedDelayScheduler delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public FixedDelayScheduler executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public FixedDelayScheduler task(Consumer<ScxScheduleStatus> task) {
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

    public Consumer<ScxScheduleStatus> task() {
        return task;
    }

    private void run() {
        try {
            long it = runCount.incrementAndGet();
            task.accept(new ScxScheduleStatus() {

            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public FixedDelayScheduler start() {
        long initialDelay;
        long delay;
        if (startTime != null) {
            initialDelay = Duration.between(startTime, Instant.now()).toNanos();
        } else {
            initialDelay = 0;
        }
        if (this.delay != null) {
            delay = this.delay.toNanos();
        } else {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        executor.scheduleWithFixedDelay(this::run, initialDelay, delay, NANOSECONDS);
        return this;
    }

}
