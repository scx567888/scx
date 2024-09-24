package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class OnceScheduler implements ScxScheduler{

    private Instant startTime;
    private Consumer<ScxScheduleStatus> task;
    private ScheduledExecutorService executor;
    private AtomicLong runCount;

    public OnceScheduler() {
        this.startTime = null;
        this.task = null;
        this.executor = null;
        this.runCount = new AtomicLong(0);
    }

    public OnceScheduler startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public OnceScheduler executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public OnceScheduler task(Consumer<ScxScheduleStatus> task) {
        this.task = task;
        return this;
    }

    public Instant startTime() {
        return startTime;
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
    public OnceScheduler start() {
        long initialDelay;
        if (startTime != null) {
            initialDelay = Duration.between(startTime, Instant.now()).toNanos();
        } else {
            initialDelay = 0;
        }
        executor.schedule(this::run, initialDelay, NANOSECONDS);
        return this;
    }
    
}
