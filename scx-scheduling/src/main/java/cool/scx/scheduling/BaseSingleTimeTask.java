package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static cool.scx.scheduling.ExpirationPolicy.*;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class BaseSingleTimeTask<T extends BaseSingleTimeTask<T>> implements ScheduleTask {

    private final System.Logger logger;

    private final AtomicLong runCount;
    private ExpirationPolicy expirationPolicy;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public BaseSingleTimeTask() {
        this.logger = System.getLogger(this.getClass().getName());
        this.runCount = new AtomicLong(0);
        this.expirationPolicy = IMMEDIATE_COMPENSATION; //默认过期补偿
        this.executor = null;
        this.task = null;
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
        var startDelay = getStartDelay();
        //判断任务是否过期
        if (startDelay < 0) {
            switch (expirationPolicy) {
                //因为在单次执行任务中 只有忽略的策略需要特殊处理
                case IMMEDIATE_IGNORE, BACKTRACKING_IGNORE -> {
                    logger.log(WARNING, "任务过期 跳过执行 !!!");
                    //这里处理一下
                    if (expirationPolicy == BACKTRACKING_IGNORE) {
                        runCount.incrementAndGet();
                    }
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
            }
        }
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

    /**
     * 延时 (单位纳秒)
     *
     * @return 延时
     */
    protected abstract long getStartDelay();

}
