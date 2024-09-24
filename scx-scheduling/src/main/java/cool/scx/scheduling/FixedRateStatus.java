package cool.scx.scheduling;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

public class FixedRateStatus implements ScheduleStatus {

    private final AtomicLong runCount;
    private ScheduledFuture<?> scheduledFuture;

    public FixedRateStatus() {
        this.runCount = new AtomicLong(0);
    }

    @Override
    public long runCount() {
        return runCount.get();
    }

    @Override
    public void cancel() {
        scheduledFuture.cancel(true);
    }

    FixedRateStatus addRunCount() {
        this.runCount.incrementAndGet();
        return this;
    }

    FixedRateStatus setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
        return this;
    }

}
