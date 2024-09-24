package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FixedRateTask implements ScheduleTask {

    protected Instant startTime;
    protected Duration delay;
    protected boolean concurrent;
    protected long maxRunCount;
    protected ScheduledExecutorService executor;
    protected Consumer<ScheduleStatus> task;
    protected final DefaultScheduleStatus status;
    protected final AtomicLong runCount;
    protected ScheduledFuture<?> scheduledFuture;

    public FixedRateTask() {
        this.startTime = null;
        this.delay = null;
        this.concurrent = false; //默认不允许并发
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.executor = null;
        this.task = null;
        this.status = new DefaultScheduleStatus();
        this.runCount = new AtomicLong(0);
    }

    public FixedRateTask startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public FixedRateTask delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public FixedRateTask concurrent(boolean concurrentExecution) {
        this.concurrent = concurrentExecution;
        return this;
    }

    @Override
    public FixedRateTask maxRunCount(long maxRunCount) {
        this.maxRunCount = maxRunCount;
        return this;
    }

    @Override
    public FixedRateTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public FixedRateTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    @Override
    public ScheduleStatus start() {
        if (this.delay == null) {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        var initialDelay = this.startTime != null ? between(this.startTime, now()).toNanos() : 0;
        var delay = this.delay.toNanos();
        this.scheduledFuture = executor.scheduleAtFixedRate(this::run, initialDelay, delay, NANOSECONDS);
        return new ScheduleStatus() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public void cancel() {
                scheduledFuture.cancel(false);
            }
        };
    }

    protected void run() {
        //如果允许并发执行则 开启虚拟线程执行
        if (concurrent) {
            Thread.ofVirtual().start(this::run0);
        } else {
            run0();
        }
    }

    protected void run0() {
        long l = this.runCount.incrementAndGet();
        //判断是否 达到最大次数 停止运行并取消任务
        if (maxRunCount != -1 && l > maxRunCount) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
            return;
        }
        try {
            // 这里传递给 task 的只是一个 copy
            task.accept(new ScheduleStatus() {
                @Override
                public long runCount() {
                    return l;
                }

                @Override
                public void cancel() {
                    scheduledFuture.cancel(false);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
