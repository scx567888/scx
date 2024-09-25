package cool.scx.scheduling.task.single_time;

import java.time.Duration;

public class DelayTaskImpl extends AbstractSingleTimeTask<DelayTaskImpl> implements DelayTask {

    private Duration delay;

    public DelayTaskImpl() {
        this.delay = null;
    }

    @Override
    public DelayTask delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    protected long getDelay() {
        return delay != null ? delay.toNanos() : 0;
    }

}
