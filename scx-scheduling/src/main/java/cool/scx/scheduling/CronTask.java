package cool.scx.scheduling;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;

public class CronTask implements ScheduleTask {

    //这里默认用 QUARTZ 的格式
    private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));

    private final AtomicLong runCount;
    private ExecutionTime executionTime;
    private boolean concurrent;
    private long maxRunCount;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;

    private final ReentrantLock scheduleNextLock;
    private ZonedDateTime lastNext = null;
    private final AtomicBoolean cancel;

    public CronTask() {
        this.runCount = new AtomicLong(0);
        this.executionTime = null;
        this.concurrent = false;// 默认不允许并发
        this.maxRunCount = -1;// 默认不限制运行次数
        this.executor = null;
        this.task = null;

        this.scheduleNextLock = new ReentrantLock();
        this.lastNext = null;
        this.cancel = new AtomicBoolean(false);
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
            e.printStackTrace();
        }
        //如果不是并发 就需要等待当前任务完成在处理下一次任务
        if (!concurrent) {
            scheduleNext();
        }
    }

    private void scheduleNext() {
        try {
            scheduleNextLock.lock();

            var now = ZonedDateTime.now();
            //1, 获取下一次执行的时间 如果为空 则直接跳过
            var next = executionTime.nextExecution(now).orElse(null);
            if (next == null) {
                return;
            }

            Duration time;
            if (lastNext != null && lastNext.isEqual(next)) {
                lastNext = executionTime.nextExecution(now).orElse(null);
                time = executionTime.timeToNextExecution(next).orElse(null);
            } else {
                lastNext = next;
                time = executionTime.timeToNextExecution(now).orElse(null);
            }

            if (time != null) {
                //此处精度不需要太高
                executor.schedule(this::run, time.toMillis(), TimeUnit.MILLISECONDS);
            }

        } finally {
            scheduleNextLock.unlock();
        }
    }
    
}
