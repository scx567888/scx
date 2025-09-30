package cool.scx.scheduling.multi_time;

import cool.scx.function.Function1Void;
import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScheduleContext;
import cool.scx.scheduling.ScheduleStatus;
import cool.scx.scheduling.TaskContext;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_RATE;
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
    private ExecutionPolicy executionPolicy;
    private long maxRunCount;
    private ExpirationPolicy expirationPolicy;
    private ScheduledExecutorService executor;
    private Function1Void<TaskContext, ?> task;
    private ScheduledFuture<?> scheduledFuture;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;
    private volatile Instant initialScheduledTime;
    private volatile Instant lastExecutionEndTime;

    public MultiTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.delay = null;
        this.executionPolicy = FIXED_RATE;//默认类型
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.expirationPolicy = IMMEDIATE_COMPENSATION;//默认策略
        this.executor = null;
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
    public MultiTimeTask task(Function1Void<TaskContext, ?> task) {
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
        if (executor == null) {
            throw new IllegalStateException("Executor 未设置 !!!");
        }
        if (delay == null) {
            throw new IllegalStateException("Delay 未设置");
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


    private ScheduleContext doStart(long startDelay) {
        this.initialScheduledTime = Instant.now().plusNanos(startDelay);
        this.scheduledFuture = switch (executionPolicy) {
            case FIXED_RATE -> executor.scheduleAtFixedRate(this::run, startDelay, delay.toNanos(), NANOSECONDS);
            case FIXED_DELAY -> executor.scheduleWithFixedDelay(this::run, startDelay, delay.toNanos(), NANOSECONDS);
        };
        this.context = new ScheduleContext() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public Instant nextRunTime() {
                if (scheduledFuture.isCancelled() || scheduledFuture.isDone()) {
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
                if (scheduledFuture.isCancelled() || scheduledFuture.isDone()) {
                    return null;
                }
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
                scheduledFuture.cancel(false);
            }

            @Override
            public ScheduleStatus status() {
                var s = scheduledFuture.state();
                return switch (s) {
                    case RUNNING -> ScheduleStatus.RUNNING;
                    case SUCCESS, FAILED -> ScheduleStatus.DONE;
                    case CANCELLED -> ScheduleStatus.CANCELLED;
                };
            }
        };
        return this.context;
    }

    private void run() {
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
            task.apply(new TaskContext() {

                @Override
                public long currentRunCount() {
                    return l;
                }

                @Override
                public ScheduleContext context() {
                    //todo 这里有可能是 null , 假设 startDelay 为 0 时 有可能先调用 run 然后有返回值
                    //是否使用锁 来强制 等待创建完成
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
