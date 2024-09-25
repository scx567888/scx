package cool.scx.scheduling;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class FixedRateTask extends MultipleTimeTask<FixedRateTask> {

    @Override
    protected ScheduledFuture<?> executorSchedule(Runnable command, long startDelay, long delay, TimeUnit unit) {
        return executor.scheduleAtFixedRate(command, startDelay, delay, unit);
    }

}
