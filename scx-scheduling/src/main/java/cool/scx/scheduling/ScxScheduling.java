package cool.scx.scheduling;

import static cool.scx.scheduling.ScxScheduler.getInstance;

public interface ScxScheduling {

    static FixedRateTask fixedRate() {
        return new FixedRateTask().executor(getInstance());
    }

    static FixedRateTask fixedDelay() {
        return new FixedDelayTask().executor(getInstance());
    }

    static CronTask cron() {
        return new CronTask().executor(getInstance());
    }

    static OnceTask once() {
        return new OnceTask().executor(getInstance());
    }

}
