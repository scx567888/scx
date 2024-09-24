package cool.scx.scheduling;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultScheduleStatus implements ScheduleStatus {

    private final AtomicLong runCount;

    private ScheduledFuture<?> scheduledFuture;

    public DefaultScheduleStatus() {
        this.runCount = new AtomicLong(0);
    }

    public long runCount() {
        return runCount.get();
    }

    public void cancel() {
        //todo 此处可以 用一个值表示是否已经取消 然后在 setScheduledFuture 中 判断如果取消了 调用取消
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }else{
            throw new RuntimeException("scheduledFuture is null");
        }
    }

    long addRunCount() {
        return this.runCount.incrementAndGet();
    }

    ScheduleStatus setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
        return this;
    }

}
