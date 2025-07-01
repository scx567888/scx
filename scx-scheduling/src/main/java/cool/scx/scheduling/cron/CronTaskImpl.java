package cool.scx.scheduling.cron;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import cool.scx.functional.ScxConsumer;
import cool.scx.scheduling.ScheduleContext;
import cool.scx.scheduling.ScheduleStatus;
import cool.scx.scheduling.TaskContext;

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
import static cool.scx.scheduling.ScheduleStatus.*;
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
    private long maxRunCount;
    private ScheduledExecutorService executor;
    private ScxConsumer<TaskContext, ?> task;
    private ZonedDateTime lastNext;
    private Consumer<Throwable> errorHandler;
    private ScheduleContext context;

    public CronTaskImpl() {
        this.runCount = new AtomicLong(0);
        this.cancel = new AtomicBoolean(false);
        this.executionTime = null;
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
    public CronTask task(ScxConsumer<TaskContext, ?> task) {
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
        if (executor == null) {
            throw new IllegalStateException("executor 未设置 !!!");
        }
        if (executionTime == null) {
            throw new IllegalStateException("execution 未设置 !!!");
        }
        // cron 不支持过期策略, 这里直接按照正常流程执行
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
            public ScheduleStatus status() {
                return cancel.get() ? CANCELLED : (runCount.get() >= maxRunCount ? DONE : RUNNING);
            }

        };
        return context;
    }

    private void scheduleNext() {

        var now = ZonedDateTime.now();

        if (lastNext == null) {
            lastNext = now;
        }

        lastNext = executionTime.nextExecution(lastNext).orElse(null);

        if (lastNext == null) {
            // 没有下一次执行时间，停止调度 这种情况很难发生
            return;
        }

        var delay = Duration.between(now, lastNext).toNanos();

        executor.schedule(this::run, delay, NANOSECONDS);

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
