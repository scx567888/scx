package cool.scx.scheduling;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public sealed interface ScheduleOptions permits CronScheduleOptions, FixedDelayScheduleOptions, FixedRateScheduleOptions, OnceScheduleOptions {

    static FixedDelayScheduleOptions ofFixedDelay() {
        return new FixedDelayScheduleOptions();
    }

    static FixedRateScheduleOptions ofFixedRate() {
        return new FixedRateScheduleOptions();
    }

    static OnceScheduleOptions ofOnce() {
        return new OnceScheduleOptions();
    }

    static OnceScheduleOptions ofOnce(LocalDateTime localDateTime) {
        return new OnceScheduleOptions().startTime(localDateTime);
    }

    static OnceScheduleOptions ofOnce(long value, ChronoUnit timeUnit) {
        return new OnceScheduleOptions().startTime(value, timeUnit);
    }

    static CronScheduleOptions ofCron(String cron) {
        return new CronScheduleOptions(cron);
    }

}
