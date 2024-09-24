package cool.scx.scheduling;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.ERROR;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class OnceTask implements ScheduleTask {

    private static final System.Logger logger = System.getLogger(FixedRateTask.class.getName());

    private final AtomicLong runCount;
    private Instant startTime;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    public OnceTask() {
        this.runCount = new AtomicLong(0);
        this.startTime = null;
        this.executor = null;
        this.task = null;
    }

    public OnceTask startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public OnceTask concurrent(boolean concurrent) {
        // 什么也不需要做
        return this;
    }

    @Override
    public OnceTask maxRunCount(long maxRunCount) {
        // 什么也不需要做
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
        var delay = startTime != null ? between(now(), startTime).toNanos() : 0;
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
