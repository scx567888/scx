package cool.scx.scheduling;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FixedDelayTask extends FixedRateTask {

    @Override
    public ScheduleStatus start() {
        if (this.delay == null) {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        var initialDelay = this.startTime != null ? between(this.startTime, now()).toNanos() : 0;
        var delay = this.delay.toNanos();
        this.scheduledFuture = executor.scheduleWithFixedDelay(this::run, initialDelay, delay, NANOSECONDS);
        return new ScheduleStatus() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public void cancel() {
                scheduledFuture.cancel(false);
            }
        };
    }

}
