package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;

import static cool.scx.scheduling.MultipleTimeTask.Type.FIXED_DELAY;
import static cool.scx.scheduling.MultipleTimeTask.Type.FIXED_RATE;
import static cool.scx.scheduling.ScxScheduler.getInstance;

/**
 * 用来创建 调度任务的工具类
 */
public interface ScxScheduling {

    static MultipleTimeTask fixedRate() {
        return new MultipleTimeTask().executor(getInstance()).type(FIXED_RATE);
    }

    static MultipleTimeTask fixedDelay() {
        return new MultipleTimeTask().executor(getInstance()).type(FIXED_DELAY);
    }

    static CronTask cron() {
        return new CronTask().executor(getInstance());
    }

    static SingleTimeTask once() {
        return new SingleTimeTask().executor(getInstance());
    }

    static ScheduleStatus setTimeout(Runnable task, long delay) {
        return once().startDelay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

    static ScheduleStatus setInterval(Runnable task, long delay) {
        return fixedRate().delay(Duration.ofMillis(delay)).start((c) -> task.run());
    }

}
