package cool.scx.scheduling.multi_time;

import cool.scx.functional.ScxConsumer;
import cool.scx.scheduling.*;
import cool.scx.timer.ScxTimer;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ConcurrencyPolicy.NO_CONCURRENCY;
import static cool.scx.scheduling.ExpirationPolicy.*;
import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_RATE;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/// 多次执行的任务 todo 代码过于复杂 待优化
///
/// @author scx567888
/// @version 0.0.1
public final class MultiTimeTaskImpl implements MultiTimeTask {

    private static final Logger logger = getLogger(MultiTimeTaskImpl.class.getName());

    private final AtomicLong runCount;
    private final AtomicBoolean cancel;
    private Supplier<Instant> startTimeSupplier;
    private Duration delay;
    private ExecutionPolicy executionPolicy;
    private ConcurrencyPolicy concurrencyPolicy;
    private long maxRunCount;
    private ExpirationPolicy expirationPolicy;
    private ScxTimer timer;
    private ScxConsumer<TaskContext, ?> task;
    private ScheduledFuture<?> scheduledFuture;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;
    private volatile Instant initialScheduledTime;
    private volatile Instant lastExecutionEndTime;

    public MultiTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.cancel = new AtomicBoolean(false);
        this.startTimeSupplier = null;
        this.delay = null;
        this.executionPolicy = FIXED_RATE;//默认类型
        this.concurrencyPolicy = NO_CONCURRENCY; //默认不允许并发
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.expirationPolicy = IMMEDIATE_COMPENSATION;//默认策略
        this.timer = null;
        this.task = null;
        this.scheduledFuture = null;
        this.errorHandler = null;
    }

    @Override
    public MultiTimeTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    public MultiTimeTask delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public MultiTimeTask executionPolicy(ExecutionPolicy executionPolicy) {
        this.executionPolicy = executionPolicy;
        return this;
    }

    @Override
    public MultiTimeTask concurrencyPolicy(ConcurrencyPolicy concurrencyPolicy) {
        this.concurrencyPolicy = concurrencyPolicy;
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
    public MultiTimeTask timer(ScxTimer timer) {
        this.timer = timer;
        return this;
    }

    @Override
    public MultiTimeTask task(ScxConsumer<TaskContext, ?> task) {
        this.task = task;
        return this;
    }

    @Override
    public MultiTimeTask onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScheduleContext start() {
        if (timer == null) {
            throw new IllegalStateException("timer 未设置 !!!");
        }
        if (delay == null) {
            throw new IllegalStateException("delay 未设置 !!!");
        }
        //此处立即获取当前时间保证准确
        var now = now();
        //获取开始时间
        var startTime = startTimeSupplier != null ? startTimeSupplier.get() : null;
        //没有开始时间 就以当前时间为开始时间
        if (startTime == null) {
            startTime = now;
        }
        //先判断过没过期
        var between = between(now, startTime);
        // 初次启动延时
        long initialDelayNanos;

        //以下处理过期情况
        if (between.isNegative()) {

            switch (expirationPolicy) {
                //1, 忽略策略
                case IMMEDIATE_IGNORE, BACKTRACKING_IGNORE -> {
                    //1, 计算过期次数和最近的开始时间
                    var delayCount = between.dividedBy(delay) * -1;
                    var nearestTime = startTime.plus(delay.multipliedBy(delayCount + 1));
                    //2, 如果是回溯忽略 我们就假设之前的已经都执行了 所以这里需要 修改 runCount
                    if (expirationPolicy == BACKTRACKING_IGNORE) {
                        runCount.addAndGet(delayCount);
                    }
                    initialDelayNanos = between(now, nearestTime).toNanos();
                }
                //2, 补偿策略
                case IMMEDIATE_COMPENSATION, BACKTRACKING_COMPENSATION -> {
                    //如果是回溯补偿则需要先把未执行的执行一遍
                    if (expirationPolicy == BACKTRACKING_COMPENSATION) {
                        var delayCount = between.dividedBy(delay) * -1;
                        //2, 执行所有过期的任务
                        for (var i = 0; i < delayCount; i = i + 1) {
                            run();
                        }
                    }
                    //补偿策略就是立即执行
                    initialDelayNanos = 0;
                }
                default -> throw new IllegalStateException("Unexpected value: " + expirationPolicy);
            }

        } else {
            initialDelayNanos = between.toNanos();
        }

        return doStart(initialDelayNanos);

    }


    private ScheduleContext doStart(long startDelay) {
        this.initialScheduledTime = Instant.now().plusNanos(startDelay);
        this.cancel.set(false);

        scheduleNext(startDelay);

        this.context = new ScheduleContext() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public Instant nextRunTime() {
                if (cancel.get()) {
                    return null;
                }
                return switch (executionPolicy) {
                    case FIXED_RATE -> initialScheduledTime.plus(delay.multipliedBy(runCount.get()));
                    case FIXED_DELAY -> {
                        Instant lastEnd = lastExecutionEndTime;
                        yield (lastEnd != null) ? lastEnd.plus(delay) : initialScheduledTime;
                    }
                };
            }

            @Override
            public Instant nextRunTime(int count) {
                if (count <= 0) {
                    throw new IllegalArgumentException("count must be positive");
                }
                if (cancel.get()) {
                    return null;
                }
                ;
                return switch (executionPolicy) {
                    case FIXED_RATE -> initialScheduledTime.plus(delay.multipliedBy(runCount.get() + count));
                    case FIXED_DELAY -> {
                        Instant next = nextRunTime();
                        yield (next != null) ? next.plus(delay.multipliedBy(count - 1)) : null;
                    }
                };
            }

            @Override
            public void cancel() {
                cancel.set(true);
            }

            @Override
            public ScheduleStatus status() {
                return cancel.get() ? ScheduleStatus.CANCELLED : (runCount.get() >= maxRunCount ? ScheduleStatus.DONE : ScheduleStatus.RUNNING);
            }
        };
        return this.context;
    }

    private void scheduleNext(long delayNanos) {
        if (cancel.get()) {
            return;
        }
        timer.runAfter(() -> {
            if (cancel.get()) {
                return;
            }
            run();
            if (maxRunCount != -1 && runCount.get() >= maxRunCount) {
                cancel.set(true);
                return;
            }
            long nextDelayNanos;
            switch (executionPolicy) {
                case FIXED_RATE -> {
                    // 下一次执行的预期时间 = 初始时间 + runCount * delay
                    Instant nextScheduledTime = initialScheduledTime.plus(delay.multipliedBy(runCount.get()));
                    long diff = Duration.between(Instant.now(), nextScheduledTime).toNanos();
                    nextDelayNanos = Math.max(diff, 0);
                }
                case FIXED_DELAY -> {
                    nextDelayNanos = delay.toNanos();
                }
                default -> nextDelayNanos = delay.toNanos();
            }
            scheduleNext(nextDelayNanos);
        }, delayNanos, NANOSECONDS);
    }

    private void run() {
        //如果允许并发执行则 开启虚拟线程执行
        switch (concurrencyPolicy) {
            case CONCURRENCY -> Thread.ofVirtual().start(this::run0);
            case NO_CONCURRENCY -> run0();
            default -> {
                //这里只可能是 null 
            }
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
            task.accept(new TaskContext() {

                @Override
                public long currentRunCount() {
                    return l;
                }

                @Override
                public ScheduleContext context() {
                    // todo 这里有可能是 null , 假设 startDelay 为 0 时 有可能先调用 run 然后有返回值 是否使用锁 来强制 等待创建完成
                    return context;
                }

            });
        } catch (Throwable e) {
            if (errorHandler != null) {
                try {
                    errorHandler.accept(e);
                } catch (Throwable ex) {
                    e.addSuppressed(ex);
                    logger.log(ERROR, "errorHandler 发生错误 !!!", e);
                }
            } else {
                logger.log(ERROR, "调度任务时发生错误 !!!", e);
            }
        }
        this.lastExecutionEndTime = Instant.now();
    }

}
