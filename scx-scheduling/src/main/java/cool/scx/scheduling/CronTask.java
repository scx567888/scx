package cool.scx.scheduling;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.lang.System.Logger;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static java.lang.System.Logger.Level.ERROR;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Cron 执行的任务
 *
 * @author scx567888
 * @version 0.0.1
 */
public class CronTask implements ScheduleTask {

    private static final Logger logger = System.getLogger(CronTask.class.getName());

    //这里默认用 QUARTZ 的格式
    private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));

    private final AtomicLong runCount;
    private final AtomicBoolean cancel;
    private ExecutionTime executionTime;
    private boolean concurrent;
    private long maxRunCount;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;
    private ZonedDateTime lastNext;


    public CronTask() {
        this.runCount = new AtomicLong(0);
        this.cancel = new AtomicBoolean(false);
        this.executionTime = null;
        this.concurrent = false;// 默认不允许并发
        this.maxRunCount = -1;// 默认不限制运行次数
        this.executor = null;
        this.task = null;
        this.lastNext = null;
    }

    public CronTask expression(String expression) {
        var cron = CRON_PARSER.parse(expression);
        this.executionTime = ExecutionTime.forCron(cron);
        return this;
    }

    @Override
    public CronTask concurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    @Override
    public CronTask maxRunCount(long maxRunCount) {
        this.maxRunCount = maxRunCount;
        return this;
    }

    @Override
    public CronTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        //不支持直接跳过
        return this;
    }

    @Override
    public CronTask executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public CronTask task(Consumer<ScheduleStatus> task) {
        this.task = task;
        return this;
    }

    @Override
    public ScheduleStatus start() {
        scheduleNext();
        return new ScheduleStatus() {
            @Override
            public long runCount() {
                return runCount.get();
            }

            @Override
            public void cancel() {
                cancel.set(true);
            }
        };
    }

    public void run() {
        var l = runCount.incrementAndGet();
        //已经取消了 或者 达到了最大次数
        if (cancel.get() || maxRunCount != -1 && l > maxRunCount) {
            return;
        }
        //如果是并发的 直接处理下一次
        if (concurrent) {
            scheduleNext();
        }
        try {
            task.accept(new ScheduleStatus() {

                @Override
                public long runCount() {
                    return l;
                }

                @Override
                public void cancel() {
                    cancel.set(true);
                }

            });
        } catch (Throwable e) {
            logger.log(ERROR, "调度任务时发生错误 !!!", e);
        }
        //如果不是并发 就需要等待当前任务完成在处理下一次任务
        if (!concurrent) {
            scheduleNext();
        }
    }

    private void scheduleNext() {

        var now = ZonedDateTime.now();

        if (lastNext == null) {
            lastNext = now;
        }

        lastNext = executionTime.nextExecution(lastNext).orElse(null);

        var delay = Duration.between(now, lastNext).toMillis();

        //此处精度不需要太高
        executor.schedule(this::run, delay, MILLISECONDS);

    }

}
