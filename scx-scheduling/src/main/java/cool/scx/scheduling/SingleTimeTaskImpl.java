package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * 单次执行的任务
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class SingleTimeTaskImpl implements SingleTimeTask {

    private static final System.Logger logger = System.getLogger(SingleTimeTaskImpl.class.getName());

    private final AtomicLong runCount;
    private Supplier<Long> startDelaySupplier;
    private ExpirationPolicy expirationPolicy;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public SingleTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startDelaySupplier = null;
        this.expirationPolicy = IMMEDIATE_COMPENSATION; //默认过期补偿
        this.executor = null;
        this.task = null;
    }

    @Override
    public SingleTimeTask startTime(Supplier<Instant> startTime) {
        this.startDelaySupplier = () -> between(now(), startTime.get()).toNanos();
        return this;
    }

    @Override
    public SingleTimeTask startTime(Instant startTime) {
        this.startDelaySupplier = () -> between(now(), startTime).toNanos();
        return this;
    }

    @Override
    public SingleTimeTask startDelay(Duration delay) {
        this.startDelaySupplier = delay::toNanos;
        return this;
    }

    @Override
    public SingleTimeTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
        return this;
    }

    @Override
    public SingleTimeTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public SingleTimeTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    @Override
    public ScheduleStatus start() {
        var startDelay = startDelaySupplier != null ? startDelaySupplier.get() : 0;
        //判断任务是否过期
        if (startDelay >= 0) {
            return doStart(startDelay);
        }

        //因为在单次执行任务中 只有忽略的策略需要特殊处理
        if (expirationPolicy == IMMEDIATE_IGNORE || expirationPolicy == BACKTRACKING_IGNORE) {
            //2, 如果是回溯忽略 我们就假设之前的已经都执行了 所以这里需要 修改 runCount
            if (expirationPolicy == BACKTRACKING_IGNORE) {
                runCount.incrementAndGet();
            }
            //单次任务 直接返回虚拟的 Status 即可 无需执行
            return new ScheduleStatus() {
                @Override
                public long runCount() {
                    return runCount.get();
                }

                @Override
                public void cancel() {
                    //任务从未执行所以无需取消
                }
            };
        }

        //单次任务的补偿策略就是立即执行
        if (expirationPolicy == IMMEDIATE_COMPENSATION || expirationPolicy == BACKTRACKING_COMPENSATION) {
            return doStart(0);
        }

        throw new IllegalStateException("Unexpected value: " + expirationPolicy);
    }

    public ScheduleStatus doStart(long startDelay) {
        var scheduledFuture = executor.schedule(this::run, startDelay, NANOSECONDS);
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

    private void run() {
        var l = runCount.incrementAndGet();
        try {
            task.accept(new ScheduleStatus() {

                @Override
                public long runCount() {
                    return l;
                }

                @Override
                public void cancel() {
                    //因为只执行一次所以没法取消也没必要取消 这里什么都不做
                }

            });
        } catch (Throwable e) {
            logger.log(ERROR, "调度任务时发生错误 !!!", e);
        }
    }

}
