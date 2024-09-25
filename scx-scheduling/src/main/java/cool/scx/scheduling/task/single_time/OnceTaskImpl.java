package cool.scx.scheduling.task.single_time;

import cool.scx.scheduling.ScheduleStatus;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class OnceTaskImpl implements OnceTask {

    private static final System.Logger logger = System.getLogger(OnceTaskImpl.class.getName());

    private final AtomicLong runCount;
    private Supplier<Instant> startTimeSupplier;
    private boolean skipIfExpired;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public OnceTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.startTimeSupplier = null;
        this.skipIfExpired = false;
        this.executor = null;
        this.task = null;
    }

    @Override
    public OnceTask startTime(Supplier<Instant> startTime) {
        this.startTimeSupplier = startTime;
        return this;
    }

    @Override
    public OnceTask skipIfExpired(boolean skipIfExpired) {
        this.skipIfExpired = skipIfExpired;
        return this;
    }

    @Override
    public OnceTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public OnceTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    @Override
    public ScheduleStatus start() {
        long delay = 0;
        if (startTimeSupplier != null) {
            var startTime = startTimeSupplier.get();
            delay = between(now(), startTime).toNanos();
        }
        //判断任务是否过期
        if (skipIfExpired && delay < 0) {
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
        var scheduledFuture = executor.schedule(this::run, delay, NANOSECONDS);
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
