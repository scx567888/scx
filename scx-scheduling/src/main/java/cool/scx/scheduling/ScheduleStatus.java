package cool.scx.scheduling;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 日程状态 包含循环的次数 (从 1 开始为第一次) 及 ScheduledFuture 可用于在方法内部中断执行
 */
public class ScheduleStatus {

    /**
     * 已执行次数
     */
    private final AtomicLong runCount = new AtomicLong(0);

    private final ScheduledFuture<?> scheduledFuture;

    public ScheduleStatus(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return scheduledFuture.cancel(mayInterruptIfRunning);
    }

    public boolean cancel() {
        return scheduledFuture.cancel(false);
    }

    public long runCount() {
        return runCount.get();
    }

    ScheduleStatus addRunCount() {
        this.runCount.incrementAndGet();
        return this;
    }

}
