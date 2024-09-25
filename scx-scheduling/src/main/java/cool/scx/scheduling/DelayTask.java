package cool.scx.scheduling;

import java.time.Duration;

public final class DelayTask extends BaseSingleTimeTask<DelayTask> {

    private Duration delay;

    public DelayTask() {
        this.delay = null;
    }

    public DelayTask startDelay(Duration delay) {
        this.delay = delay;
        return this;
    }

    protected long getStartDelay() {
        return delay != null ? delay.toNanos() : 0;
    }

}
