package cool.scx.scheduling.task.single_time;

import cool.scx.scheduling.ScheduleStatus;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class AbstractSingleTimeTask<T extends AbstractSingleTimeTask<T>> implements SingleTimeTask {

    private final System.Logger logger;

    private final AtomicLong runCount;
    private boolean skipIfExpired;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public AbstractSingleTimeTask() {
        this.logger = System.getLogger(this.getClass().getName());
        this.runCount = new AtomicLong(0);
        this.skipIfExpired = false;
        this.executor = null;
        this.task = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final T skipIfExpired(boolean skipIfExpired) {
        this.skipIfExpired = skipIfExpired;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final T executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final T task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return (T) this;
    }

    @Override
    public final ScheduleStatus start() {
        var startDelay = getStartDelay();
        //判断任务是否过期
        if (skipIfExpired && startDelay < 0) {
            logger.log(WARNING, "任务过期 跳过执行 !!!");
            return new ScheduleStatus() {
                @Override
                public long runCount() {
                    return 0;
                }

                @Override
                public void cancel() {
                    //任务从未执行所以无需取消
                }
            };
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
