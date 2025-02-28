package cool.scx.scheduling;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/// 多次执行的任务
///
/// @author scx567888
/// @version 0.0.1
public final class MultiTimeTaskImpl implements MultiTimeTask {

    private static final Logger logger = getLogger(MultiTimeTaskImpl.class.getName());

    private final AtomicLong runCount;
    private Supplier<Instant> startTimeSupplier;
    private Duration delay;
    private Type type;
    private boolean concurrent;
    private long maxRunCount;
    private ExpirationPolicy expirationPolicy;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;
    private ScheduledFuture<?> scheduledFuture;

    public MultiTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.delay = null;
        this.type = Type.FIXED_RATE;//默认类型
        this.concurrent = false; //默认不允许并发
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.expirationPolicy = IMMEDIATE_COMPENSATION;//默认策略
        this.executor = null;
        this.task = null;
        this.scheduledFuture = null;
    }

    @Override
    public MultiTimeTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    public MultiTimeTask startTime(Instant startTime) {
        this.startTimeSupplier = () -> startTime;
        return this;
    }

    @Override
    public MultiTimeTask delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public MultiTimeTask type(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public MultiTimeTask concurrent(boolean concurrentExecution) {
        this.concurrent = concurrentExecution;
        return this;
    }

    @Override
    public MultiTimeTask maxRunCount(long maxRunCount) {
        this.maxRunCount = maxRunCount;
        return this;
    }

    @Override
    public MultiTimeTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
        return this;
    }

    @Override
    public MultiTimeTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public MultiTimeTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    @Override
    public ScheduleStatus start() {
        if (this.delay == null) {
            throw new IllegalArgumentException("Delay must be non-null");
        }
        //此处立即获取当前时间保证准确行
        var now = now();
        //获取开始时间
        var startTime = this.startTimeSupplier != null ? this.startTimeSupplier.get() : null;
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

        //以下处理过期情况 
        //1, 忽略策略
        if (expirationPolicy == IMMEDIATE_IGNORE || expirationPolicy == BACKTRACKING_IGNORE) {
            //1, 计算过期次数和最近的开始时间
            var delayCount = between.dividedBy(delay) * -1;
            var nearestTime = startTime.plus(delay.multipliedBy(delayCount + 1));
            //2, 如果是回溯忽略 我们就假设之前的已经都执行了 所以这里需要 修改 runCount
            if (expirationPolicy == BACKTRACKING_IGNORE) {
                runCount.addAndGet(delayCount);
            }
            return doStart(between(now, nearestTime).toNanos());
        }
        //2, 补偿策略
        if (expirationPolicy == IMMEDIATE_COMPENSATION || expirationPolicy == BACKTRACKING_COMPENSATION) {
            //如果是回溯补偿则需要先把未执行的执行一遍
            if (expirationPolicy == BACKTRACKING_COMPENSATION) {
                var delayCount = between.dividedBy(delay) * -1;
                //2, 执行所有过期的任务
                for (var i = 0; i < delayCount; i = i + 1) {
                    run();
                }
            }
            return doStart(0);
        }
        throw new IllegalStateException("Unexpected value: " + expirationPolicy);
    }

    private void run() {
        //如果允许并发执行则 开启虚拟线程执行
        if (concurrent) {
            executor.execute(this::run0);
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

    private ScheduleStatus doStart(long startDelay) {
        this.scheduledFuture = switch (type) {
            case FIXED_RATE -> executor.scheduleAtFixedRate(this::run, startDelay, delay.toNanos(), NANOSECONDS);
            case FIXED_DELAY -> executor.scheduleWithFixedDelay(this::run, startDelay, delay.toNanos(), NANOSECONDS);
        };
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

}
