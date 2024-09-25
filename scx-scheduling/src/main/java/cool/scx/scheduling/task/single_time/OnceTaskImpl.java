package cool.scx.scheduling.task.single_time;

import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Duration.between;
import static java.time.Instant.now;

public class OnceTaskImpl extends AbstractSingleTimeTask<OnceTaskImpl> implements OnceTask {

    private Supplier<Instant> startTimeSupplier;

    public OnceTaskImpl() {
        this.startTimeSupplier = null;
    }

    @Override
    public OnceTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    protected long getStartDelay() {
        return startTimeSupplier != null ? between(now(), startTimeSupplier.get()).toNanos() : 0;
    }

}
