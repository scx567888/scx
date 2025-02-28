package cool.scx.scheduling.single_time;

import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScheduleStatus;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/// 单次执行的任务
///
/// @author scx567888
/// @version 0.0.1
public final class SingleTimeTaskImpl implements SingleTimeTask {

    private static final Logger logger = getLogger(SingleTimeTaskImpl.class.getName());

    private final AtomicLong runCount;
    private Supplier<Instant> startTimeSupplier;
    private ExpirationPolicy expirationPolicy;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public SingleTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.expirationPolicy = IMMEDIATE_COMPENSATION; //默认过期补偿
        this.executor = null;
        this.task = null;
    }

    @Override
    public SingleTimeTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    public SingleTimeTask startTime(Instant startTime) {
        this.startTimeSupplier = () -> startTime;
        return this;
    }

    @Override
    public SingleTimeTask startDelay(Duration delay) {
        this.startTimeSupplier = () -> now().plus(delay);
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
        if (executor == null) {
            throw new IllegalStateException("executor 未设置 !!!");
        }
        //此处立即获取当前时间保证准确
        var now = now();
        //获取开始时间
        var startTime = startTimeSupplier != null ? startTimeSupplier.get() : null;
        //没有开始时间 就不需要验证任何过期策略 直接执行
        if (startTime == null) {
            return doStart(0);
        }
        //先判断过没过期
        var between = between(now, startTime);
        //不为负数 则没有过期
        if (!between.isNegative()) {
            return doStart(between.toNanos());
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
                public Instant nextRunTime() {
                    return null; // 忽略策略没有下一次运行时间
                }

                @Override
                public Instant nextRunTime(int count) {
                    return null;// 同上
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

    private ScheduleStatus doStart(long startDelay) {
        // 计算任务的实际启动时间
        var scheduledTime = now().plusNanos(startDelay);
        // 调用任务
        var scheduledFuture = executor.schedule(this::run, startDelay, NANOSECONDS);
        return new ScheduleStatus() {

            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public Instant nextRunTime() {
                // 如果任务已执行，则没有下一次运行时间
                return runCount() > 0 ? null : scheduledTime;
            }

            @Override
            public Instant nextRunTime(int count) {
                return count == 1 ? nextRunTime() : null;
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
                public Instant nextRunTime() {
                    return null;// 因为只执行一次所以没有下一次 这里返回 null
                }

                @Override
                public Instant nextRunTime(int count) {
                    return null;// 同上
                }

                @Override
                public void cancel() {
                    // 因为只执行一次所以没法取消也没必要取消 这里什么都不做
                }

            });
        } catch (Throwable e) {
            logger.log(ERROR, "调度任务时发生错误 !!!", e);
        }
    }

}
