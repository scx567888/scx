package cool.scx.scheduling;

import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Duration.between;
import static java.time.Instant.now;

public final class OnceTask extends SingleTimeTask<OnceTask> {

    private Supplier<Instant> startTimeSupplier;

    public OnceTask() {
        this.startTimeSupplier = null;
    }

    /**
     * 开始时间 这里采用 Supplier 保证不会因为方法执行的速度导致时间误差
     *
     * @param startTime startTime
     * @return a
     */
    public OnceTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    public OnceTask startTime(Instant startTime) {
        this.startTimeSupplier = () -> startTime;
        return this;
    }

    @Override
    protected long getStartDelay() {
        return startTimeSupplier != null ? between(now(), startTimeSupplier.get()).toNanos() : 0;
    }

}
