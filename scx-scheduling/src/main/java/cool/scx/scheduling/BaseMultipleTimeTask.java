package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * 可多次执行的任务
 */
public abstract class BaseMultipleTimeTask<T extends BaseMultipleTimeTask<T>> implements MultipleTimeTask {

    private static final System.Logger logger = System.getLogger(FixedRateTask.class.getName());

    private final AtomicLong runCount;
    protected ScheduledExecutorService executor;
    private Supplier<Instant> startTimeSupplier;
    private Duration delay;
    private ExpirationPolicy expirationPolicy;
    private boolean concurrent;
    private long maxRunCount;
    private Consumer<ScheduleStatus> task;
    private ScheduledFuture<?> scheduledFuture;

    public BaseMultipleTimeTask() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.delay = null;
        this.expirationPolicy = IMMEDIATE_COMPENSATION;
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
    public T startTime(Instant startTime) {
        this.startTimeSupplier = () -> startTime;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T delay(Duration delay) {
        this.delay = delay;
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
    public T expirationPolicy(ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
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
        //此处立即获取当前时间保证准确行
        var now = now();
        //获取开始时间
        var startTime = this.startTimeSupplier != null ? this.startTimeSupplier.get() : null;
        //没有开始时间 就不需要验证任何过期策略 直接执行
        if (startTime == null) {
            this.scheduledFuture = executorSchedule(this::run, 0, delay.toNanos(), NANOSECONDS);
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
        //先判断过没过期
        var between = between(now, startTime);
        //不为负数 则没有过期
        if (!between.isNegative()) {
            this.scheduledFuture = executorSchedule(this::run, between.toNanos(), delay.toNanos(), NANOSECONDS);
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
        
        //处理过期情况 
        switch (expirationPolicy) {
            case IMMEDIATE_IGNORE, BACKTRACKING_IGNORE -> {
                //1, 计算过期次数
                var delayCount = between.dividedBy(delay) * -1;
                //2, 计算下一次最近的时间点 用作开始时间
                var nearestTime = startTime.plus(delay.multipliedBy(delayCount + 1));
                //3, 计算延时
                var d = between(now, nearestTime);
                //4, 如果是回溯忽略 我们就假设之前的已经都执行了 所以这里需要 处理 runCount
                if (expirationPolicy == BACKTRACKING_IGNORE) {
                    runCount.addAndGet(delayCount);
                }
                this.scheduledFuture = executorSchedule(this::run, d.toNanos(), delay.toNanos(), NANOSECONDS);
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

            //2, 立即补偿的行为其实和 默认是一样的
            case IMMEDIATE_COMPENSATION -> {
                this.scheduledFuture = executorSchedule(this::run, 0, delay.toNanos(), NANOSECONDS);
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
            //3, 回溯补偿
            case BACKTRACKING_COMPENSATION -> {
                //1, 计算过期次数
                var delayCount = between.dividedBy(delay) * -1;
                //2, 执行所有过期的任务
                for (int i = 0; i < delayCount; i = i + 1) {
                    run();
                }
                this.scheduledFuture = executorSchedule(this::run, 0, delay.toNanos(), NANOSECONDS);
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
            default -> throw new IllegalStateException("Unexpected value: " + expirationPolicy);
        }
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
