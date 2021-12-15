package cool.scx.scheduler;

import cool.scx.ScxHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 固定次数执行的 Runnable
 */
class FixedExecutionRunnable implements Runnable {

    /**
     * 已执行次数
     */
    private final AtomicLong runCount = new AtomicLong();

    /**
     * 最大执行次数
     */
    private final long maxRunCount;

    /**
     * 执行 scxHandler
     */
    private final ScxHandler<ScheduleStatus> scxHandler;

    /**
     * ScheduledFuture 对象 用于手动取消
     */
    private ScheduledFuture<?> scheduledFuture = null;

    public FixedExecutionRunnable(ScxHandler<ScheduleStatus> scxHandler, long maxRunCount) {
        this.maxRunCount = maxRunCount;
        this.scxHandler = scxHandler;
    }

    @Override
    public void run() {
        scxHandler.handle(new ScheduleStatus(runCount.get(), scheduledFuture));
        if (runCount.getAndIncrement() >= maxRunCount) {
            scheduledFuture.cancel(false);
        }
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(EventLoopGroup executor, long delay, long period) {
        scheduledFuture = executor.scheduleWithFixedDelay(this, delay, period, TimeUnit.MILLISECONDS);
        return scheduledFuture;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(EventLoopGroup executor, long delay, long period) {
        scheduledFuture = executor.scheduleAtFixedRate(this, delay, period, TimeUnit.MILLISECONDS);
        return scheduledFuture;
    }

}