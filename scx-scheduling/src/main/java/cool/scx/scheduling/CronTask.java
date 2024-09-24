package cool.scx.scheduling;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;

public class CronTask implements ScheduleTask {
    
    //这里默认用 QUARTZ 的格式
    private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));
    
    private ExecutionTime executionTime;
    private boolean concurrent;
    private long maxRunCount;
    private ScheduledExecutorService executor;
    private Consumer<ScheduleStatus> task;
    private final ReentrantLock scheduleNextLock;
    private ZonedDateTime lastNext = null;

    public CronTask() {
        this.executionTime = null;
        this.concurrent = false;// 默认不允许并发
        this.maxRunCount = -1;// 默认不限制运行次数
        this.executor = null;
        this.task = null;
        this.scheduleNextLock = new ReentrantLock();
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
        this.maxRunCount=maxRunCount;
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

    
    
    //todo 
    @Override
    public ScheduleStatus start() {
        scheduleNext();
        return this.status;
    }
    

    public void run() {
        if (concurrentExecution) {
            scheduleNext();
        }
        try {
//            task.accept(this.status.addRunCount());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (!concurrentExecution) {
            scheduleNext();
        }
    }


    private void scheduleNext() {
        try {
            scheduleNextLock.lock();

            var now = ZonedDateTime.now();
            //1, 获取下一次执行的事件 如果为空 则直接跳过
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
                var scheduledFuture = executor.schedule(this::run, time.toMillis(), TimeUnit.MILLISECONDS);
                this.status.setScheduledFuture(scheduledFuture);
            }

        } finally {
            scheduleNextLock.unlock();
        }
    }


}
