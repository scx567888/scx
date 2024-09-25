package cool.scx.scheduling;

import static cool.scx.scheduling.ScxScheduler.getInstance;

/**
 * 用来创建 调度任务的工具类
 */
public interface ScxScheduling {

    static FixedRateTask fixedRate() {
        return new FixedRateTask().executor(getInstance());
    }

    static FixedDelayTask fixedDelay() {
        return new FixedDelayTask().executor(getInstance());
    }

    static CronTask cron() {
        return new CronTask().executor(getInstance());
    }

    static OnceTask once() {
        return new OnceTask().executor(getInstance());
    }

    static DelayTask delay() {
        return new DelayTask().executor(getInstance());
    }

}
