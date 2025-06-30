package cool.scx.scheduling.multi_time;

import cool.scx.functional.ScxConsumer;
import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScheduleContext;
import cool.scx.scheduling.ScheduleStatus;
import cool.scx.scheduling.TaskContext;
import cool.scx.timer.ScxTimer;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FixedRateTask implements MultiTimeTask {

    private static final Logger logger = getLogger(FixedRateTask.class.getName());

    private final AtomicLong runCount;
    private final AtomicBoolean cancel;
    private Supplier<Instant> startTimeSupplier;
    private Duration delay;
    private long maxRunCount;
    private ExpirationPolicy expirationPolicy;
    private ScxTimer timer;
    private ScxConsumer<TaskContext, ?> task;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;
    private volatile Instant initialScheduledTime;
    private volatile Instant lastExecutionEndTime;

    public FixedRateTask() {
        this.runCount = new AtomicLong(0);
        this.cancel = new AtomicBoolean(false);
        this.startTimeSupplier = null;
        this.delay = null;
        this.maxRunCount = -1;// 默认没有最大运行次数
        this.expirationPolicy = IMMEDIATE_COMPENSATION;//默认策略
        this.timer = null;
        this.task = null;
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
                            //根据是否并发执行
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
                return initialScheduledTime.plus(delay.multipliedBy(runCount.get()));
            }

            @Override
            public Instant nextRunTime(int count) {
                if (count <= 0) {
                    throw new IllegalArgumentException("count must be positive");
                }
                if (cancel.get()) {
                    return null;
                }
                return initialScheduledTime.plus(delay.multipliedBy(runCount.get() + count));
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

    private void scheduleNext() {

        var now = ZonedDateTime.now();

        var delay = Duration.between(now, lastNext).toNanos();

        timer.runAfter(this::run, delayNanos, NANOSECONDS);

    }

    private void run() {
        var l = runCount.incrementAndGet();
        // 已经取消了 或者 达到了最大次数
        if (cancel.get() || maxRunCount != -1 && l > maxRunCount) {
            return;
        }
        //直接处理下一次
        scheduleNext();
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
            handleTaskError(e);
        }
        this.lastExecutionEndTime = Instant.now();
    }

    private void handleTaskError(Throwable e) {
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

}
