package cool.scx.scheduling;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class OnceTask implements ScheduleTask {

    private final DefaultScheduleStatus status;
    private Instant startTime;
    private Consumer<ScheduleStatus> task;
    private ScheduledExecutorService executor;

    public OnceTask() {
        this.status = new DefaultScheduleStatus();
        this.startTime = null;
        this.task = null;
        this.executor = null;
    }

    public OnceTask startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public OnceTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public OnceTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    public Instant startTime() {
        return startTime;
    }

    public ScheduledExecutorService executor() {
        return executor;
    }

    public Consumer<ScheduleStatus> task() {
        return task;
    }

    private void run() {
        try {
            task.accept(this.status.addRunCount());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public ScheduleStatus start() {
        var delay = startTime != null ? between(now(), startTime).toNanos() : 0;
        var scheduledFuture = executor.schedule(this::run, delay, NANOSECONDS);
        return this.status.setScheduledFuture(scheduledFuture);
    }

}
