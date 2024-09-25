package cool.scx.scheduling;

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

    static OnceTask once() {
        return new OnceTask().executor(getInstance());
    }

    static DelayTask delay() {
        return new DelayTask().executor(getInstance());
    }

}
