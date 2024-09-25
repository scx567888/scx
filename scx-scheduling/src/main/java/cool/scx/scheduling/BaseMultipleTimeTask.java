package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.ERROR;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * 可多次执行的任务
 */
public abstract class BaseMultipleTimeTask<T extends BaseMultipleTimeTask<T>> implements ScheduleTask {

    private static final System.Logger logger = System.getLogger(FixedRateTask.class.getName());

    private final AtomicLong runCount;
    protected ScheduledExecutorService executor;
    private Supplier<Instant> startTimeSupplier;
    private boolean skipIfExpired;
    private Duration delay;
    private boolean concurrent;
    private long maxRunCount;
    private Consumer<ScheduleStatus> task;
    private ScheduledFuture<?> scheduledFuture;

    public BaseMultipleTimeTask() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.skipIfExpired = false;
        this.delay = null;
        this.concurrent = false; //默认不允许并发
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.executor = null;
        this.task = null;
        this.scheduledFuture = null;
    }

    @SuppressWarnings("unchecked")
    public T startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T delay(Duration delay) {
        this.delay = delay;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T skipIfExpired(boolean skipIfExpired) {
        this.skipIfExpired = skipIfExpired;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T concurrent(boolean concurrentExecution) {
        this.concurrent = concurrentExecution;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T maxRunCount(long maxRunCount) {
        this.maxRunCount = maxRunCount;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return (T) this;
    }

    @Override
    public ScheduleStatus start() {
        if (this.delay == null) {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        var startDelay = getStartDelay();
        var delay = this.delay.toNanos();
        this.scheduledFuture = executorSchedule(this::run, startDelay, delay, NANOSECONDS);
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

    private long getStartDelay() {
        return this.startTimeSupplier != null ? between(now(), this.startTimeSupplier.get()).toNanos() : 0;
    }

    private void run() {
        //如果允许并发执行则 开启虚拟线程执行
        if (concurrent) {
            Thread.ofVirtual().start(this::run0);
        } else {
            run0();
        }
    }

    private void run0() {
        var l = runCount.incrementAndGet();
        //判断是否 达到最大次数 停止运行并取消任务
        if (maxRunCount != -1 && l > maxRunCount) {
            //todo 这里 scheduledFuture 可能为空吗 ? 
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
            return;
        }
        try {
            task.accept(new ScheduleStatus() {

                @Override
                public long runCount() {
                    return l;
                }

                @Override
                public void cancel() {
                    // todo 这里也是 可能为空吗?
                    scheduledFuture.cancel(false);
                }

            });
        } catch (Throwable e) {
            logger.log(ERROR, "调度任务时发生错误 !!!", e);
        }
    }

    protected abstract ScheduledFuture<?> executorSchedule(Runnable command, long startDelay, long delay, TimeUnit unit);

}
