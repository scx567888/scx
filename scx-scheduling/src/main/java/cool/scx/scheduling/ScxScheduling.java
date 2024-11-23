package cool.scx.scheduling;

import java.time.Duration;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static cool.scx.scheduling.MultipleTimeTask.Type.FIXED_DELAY;
import static cool.scx.scheduling.MultipleTimeTask.Type.FIXED_RATE;

/**
 * 用来创建 调度任务的工具类
 */
public final class ScxScheduling {

    private static ScheduledThreadPoolExecutor defaultScheduler;

    public static ScheduledThreadPoolExecutor defaultScheduler() {
        if (defaultScheduler == null) {
            defaultScheduler = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
        }
        return defaultScheduler;
    }

    public static MultipleTimeTask fixedRate() {
        return new MultipleTimeTask().executor(defaultScheduler()).type(FIXED_RATE);
    }

    public static MultipleTimeTask fixedDelay() {
        return new MultipleTimeTask().executor(defaultScheduler()).type(FIXED_DELAY);
    }

    public static CronTask cron() {
        return new CronTask().executor(defaultScheduler());
    }

    public static SingleTimeTask once() {
        return new SingleTimeTask().executor(defaultScheduler());
    }

    public static ScheduleStatus setTimeout(Runnable task, long delay) {
        return once().startDelay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

    public static ScheduleStatus setInterval(Runnable task, long delay) {
        return fixedRate().delay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

}
