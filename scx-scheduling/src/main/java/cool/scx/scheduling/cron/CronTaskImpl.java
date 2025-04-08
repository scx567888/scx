package cool.scx.scheduling.cron;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import cool.scx.scheduling.ConcurrencyPolicy;
import cool.scx.scheduling.ScheduleContext;
import cool.scx.scheduling.Task;
import cool.scx.scheduling.TaskStatus;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static cool.scx.scheduling.ConcurrencyPolicy.CONCURRENCY;
import static cool.scx.scheduling.ConcurrencyPolicy.NO_CONCURRENCY;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/// Cron 执行的任务
///
/// @author scx567888
/// @version 0.0.1
public class CronTaskImpl implements CronTask {

    private static final Logger logger = getLogger(CronTaskImpl.class.getName());

    // 这里默认用 QUARTZ 的格式
    private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));

    private final AtomicLong runCount;
    private final AtomicBoolean cancel;
    private ExecutionTime executionTime;
    private ConcurrencyPolicy concurrencyPolicy;
    private long maxRunCount;
    private ScheduledExecutorService executor;
    private Task task;
    private ZonedDateTime lastNext;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;

    public CronTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.cancel = new AtomicBoolean(false);
        this.executionTime = null;
        this.concurrencyPolicy = NO_CONCURRENCY; // 默认不允许并发
        this.maxRunCount = -1; // 默认不限制运行次数
        this.executor = null;
        this.task = null;
        this.lastNext = null;
        this.errorHandler = null;
        this.context = null;
    }

    @Override
    public CronTask expression(String expression) {
        var cron = CRON_PARSER.parse(expression);
        this.executionTime = ExecutionTime.forCron(cron);
        return this;
    }

    @Override
    public CronTask concurrencyPolicy(ConcurrencyPolicy concurrencyPolicy) {
        this.concurrencyPolicy = concurrencyPolicy;
        return this;
    }

    @Override
    public CronTask maxRunCount(long maxRunCount) {
        this.maxRunCount = maxRunCount;
        return this;
    }

    @Override
    public CronTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public CronTask task(Task task) {
        this.task = task;
        return this;
    }

    @Override
    public CronTask onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScheduleContext start() {
        scheduleNext();
        this.context = new ScheduleContext() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public Instant nextRunTime() {
                return lastNext != null ? lastNext.toInstant() : null;
            }

            @Override
            public Instant nextRunTime(int count) {
                ZonedDateTime nextTime = lastNext;
                for (int i = 0; i < count; i = i + 1) {
                    nextTime = executionTime.nextExecution(nextTime).orElse(null);
                }
                return nextTime != null ? nextTime.toInstant() : null;
            }

            @Override
            public void cancel() {
                cancel.set(true);
            }

            @Override
            public Status status() {
                return cancel.get() ? Status.CANCELED : (runCount.get() >= maxRunCount ? Status.DONE : Status.RUNNING);
            }
        };
        return context;
    }

    public void run() {
        var l = runCount.incrementAndGet();
        // 已经取消了 或者 达到了最大次数
        if (cancel.get() || maxRunCount != -1 && l > maxRunCount) {
            return;
        }
        //如果是并发的 直接处理下一次
        if (concurrencyPolicy == CONCURRENCY) {
            scheduleNext();
        }
        try {
            task.run(new TaskStatus() {

                @Override
                public long currentRunCount() {
                    return l;
                }

                @Override
                public ScheduleContext context() {
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
        //如果不是并发 就需要等待当前任务完成在处理下一次任务
        if (concurrencyPolicy != CONCURRENCY) {
            scheduleNext();
        }
    }

    private void scheduleNext() {

        var now = ZonedDateTime.now();

        if (lastNext == null) {
            lastNext = now;
        }

        lastNext = executionTime.nextExecution(lastNext).orElse(null);

        var delay = Duration.between(now, lastNext).toNanos();

        executor.schedule(this::run, delay, NANOSECONDS);

    }

}
