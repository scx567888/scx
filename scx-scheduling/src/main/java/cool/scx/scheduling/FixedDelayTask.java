package cool.scx.scheduling;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class FixedDelayTask extends BaseMultipleTimeTask<FixedDelayTask> {

    @Override
    protected ScheduledFuture<?> executorSchedule(Runnable command, long startDelay, long delay, TimeUnit unit) {
        return executor.scheduleWithFixedDelay(command, startDelay, delay, unit);
    }

}
