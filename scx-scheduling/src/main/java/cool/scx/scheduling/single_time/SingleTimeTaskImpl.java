package cool.scx.scheduling.single_time;

import cool.scx.functional.ScxConsumer;
import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScheduleContext;
import cool.scx.scheduling.ScheduleStatus;
import cool.scx.scheduling.TaskContext;
import cool.scx.timer.ScxTimer;

import java.lang.System.Logger;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.BACKTRACKING_IGNORE;
import static cool.scx.scheduling.ExpirationPolicy.IMMEDIATE_COMPENSATION;
import static cool.scx.timer.TaskStatus.CANCELLED;
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
    private ScxTimer timer;
    private ScxConsumer<TaskContext, ?> task;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;

    public SingleTimeTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.expirationPolicy = IMMEDIATE_COMPENSATION; //默认过期补偿
        this.timer = null;
        this.task = null;
        this.errorHandler = null;
        this.context = null;
    }

    @Override
    public SingleTimeTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    public SingleTimeTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
        return this;
    }

    @Override
    public SingleTimeTask timer(ScxTimer timer) {
        this.timer = timer;
        return this;
    }

    @Override
    public SingleTimeTask task(ScxConsumer<TaskContext, ?> task) {
        this.task = task;
        return this;
    }

    @Override
    public SingleTimeTask onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScheduleContext start() {
        if (timer == null) {
            throw new IllegalStateException("timer 未设置 !!!");
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
                    //2, 如果是回溯忽略 我们就假设之前的已经都执行了 所以这里需要 修改 runCount
                    if (expirationPolicy == BACKTRACKING_IGNORE) {
                        runCount.incrementAndGet();
                    }
                    //单次任务 直接返回虚拟的 Status 即可 无需执行
                    return new ScheduleContext() {
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

                        @Override
                        public ScheduleStatus status() {
                            return null;
                        }

                    };
                    //单次任务 直接返回虚拟的 Status 即可 无需执行
                }
                //2, 补偿策略
                case IMMEDIATE_COMPENSATION, BACKTRACKING_COMPENSATION -> {
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
        // 计算任务的实际启动时间
        var scheduledTime = now().plusNanos(startDelay);
        // 调用任务
        var taskHandle = timer.runAfter(this::run, startDelay, NANOSECONDS);
        this.context = new ScheduleContext() {

            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public Instant nextRunTime() {
                // 任务取消 没有下一次执行时间
                if (taskHandle.status() == CANCELLED) {
                    return null;
                }
                // 如果任务已执行, 则没有下一次运行时间
                if (runCount() > 0) {
                    return null;
                }
                return scheduledTime;
            }

            @Override
            public Instant nextRunTime(int count) {
                return count == 1 ? nextRunTime() : null;
            }

            @Override
            public void cancel() {
                taskHandle.cancel();
            }

            @Override
            public ScheduleStatus status() {
                var s = taskHandle.status();
                return switch (s) {
                    case PENDING, RUNNING -> ScheduleStatus.RUNNING;
                    case SUCCESS, FAILED -> ScheduleStatus.DONE;
                    case CANCELLED -> ScheduleStatus.CANCELLED;
                };
            }

        };
        return this.context;
    }

    private void run() {
        var l = runCount.incrementAndGet();
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
    }

}
