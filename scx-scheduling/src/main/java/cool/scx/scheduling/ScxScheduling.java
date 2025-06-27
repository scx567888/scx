package cool.scx.scheduling;

import cool.scx.scheduling.cron.CronTask;
import cool.scx.scheduling.cron.CronTaskImpl;
import cool.scx.scheduling.multi_time.MultiTimeTask;
import cool.scx.scheduling.multi_time.MultiTimeTaskImpl;
import cool.scx.scheduling.single_time.SingleTimeTask;
import cool.scx.scheduling.single_time.SingleTimeTaskImpl;
import cool.scx.timer.ScheduledExecutorTimer;
import cool.scx.timer.ScxTimer;

import java.time.Duration;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_DELAY;
import static cool.scx.scheduling.multi_time.ExecutionPolicy.FIXED_RATE;


/// 用来创建 调度任务的工具类
///
/// @author scx567888
/// @version 0.0.1
public final class ScxScheduling {

    private static ScxTimer DEFAULT_TIMER;

    public static ScxTimer defaultTimer() {
        if (DEFAULT_TIMER == null) {
            DEFAULT_TIMER = new ScheduledExecutorTimer(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2));
        }
        return DEFAULT_TIMER;
    }

    public static MultiTimeTask fixedRate() {
        return new MultiTimeTaskImpl().timer(defaultTimer()).executionPolicy(FIXED_RATE);
    }

    public static MultiTimeTask fixedDelay() {
        return new MultiTimeTaskImpl().timer(defaultTimer()).executionPolicy(FIXED_DELAY);
    }

    public static CronTask cron() {
        return new CronTaskImpl().timer(defaultTimer());
    }

    public static SingleTimeTask once() {
        return new SingleTimeTaskImpl().timer(defaultTimer());
    }

    public static ScheduleContext setTimeout(Runnable task, long delay) {
        return once().startDelay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

    public static ScheduleContext setInterval(Runnable task, long delay) {
        return fixedRate().delay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

}
