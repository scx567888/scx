package cool.scx.scheduling;

import cool.scx.scheduling.cron.CronTask;
import cool.scx.scheduling.cron.CronTaskImpl;
import cool.scx.scheduling.multi_time.MultiTimeTask;
import cool.scx.scheduling.multi_time.MultiTimeTaskImpl;
import cool.scx.scheduling.single_time.SingleTimeTask;
import cool.scx.scheduling.single_time.SingleTimeTaskImpl;

import java.time.Duration;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_DELAY;
import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_RATE;

/// 用来创建 调度任务的工具类
///
/// @author scx567888
/// @version 0.0.1
public final class ScxScheduling {

    private static ScheduledThreadPoolExecutor defaultScheduler;

    public static ScheduledThreadPoolExecutor defaultScheduler() {
        if (defaultScheduler == null) {
            defaultScheduler = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
        }
        return defaultScheduler;
    }

    public static MultiTimeTask fixedRate() {
        return new MultiTimeTaskImpl().executor(defaultScheduler()).executionPolicy(FIXED_RATE);
    }

    public static MultiTimeTask fixedDelay() {
        return new MultiTimeTaskImpl().executor(defaultScheduler()).executionPolicy(FIXED_DELAY);
    }

    public static CronTask cron() {
        return new CronTaskImpl().executor(defaultScheduler());
    }

    public static SingleTimeTask once() {
        return new SingleTimeTaskImpl().executor(defaultScheduler());
    }

    public static ScheduleContext setTimeout(Runnable task, long delay) {
        return once().startDelay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

    public static ScheduleContext setInterval(Runnable task, long delay) {
        return fixedRate().delay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

}
