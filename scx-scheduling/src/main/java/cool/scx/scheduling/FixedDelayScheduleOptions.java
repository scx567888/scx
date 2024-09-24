package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public non-sealed class FixedDelayScheduleOptions implements ScheduleOptions {


    private Instant startTime;
    private Duration delay;

    public FixedDelayScheduleOptions() {
        this.startTime = Instant.now();
    }

    public FixedDelayScheduleOptions startTime(LocalDateTime localDateTime) {
        this.startTime = localDateTime.toInstant(ZoneOffset.ofHours(8));
        return this;
    }

    public FixedDelayScheduleOptions startTime(long value, ChronoUnit timeUnit) {
        this.startTime = Instant.now().plus(value, timeUnit);
        return this;
    }

    public FixedDelayScheduleOptions delay(long value, ChronoUnit timeUnit) {
        this.delay = Duration.of(value, timeUnit);
        return this;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Duration getDelay() {
        if (this.delay == null) {
            throw new IllegalStateException("未设置延迟时间 !!!");
        }
        return delay;
    }

}
