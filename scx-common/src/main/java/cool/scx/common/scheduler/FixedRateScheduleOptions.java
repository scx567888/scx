package cool.scx.common.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public non-sealed class FixedRateScheduleOptions implements ScheduleOptions {

    private Instant startTime;
    private Duration delay;

    public FixedRateScheduleOptions() {
        this.startTime = Instant.now();
    }

    public FixedRateScheduleOptions startTime(LocalDateTime localDateTime) {
        this.startTime = localDateTime.toInstant(ZoneOffset.ofHours(8));
        return this;
    }

    public FixedRateScheduleOptions startTime(long value, ChronoUnit timeUnit) {
        this.startTime = Instant.now().plus(value, timeUnit);
        return this;
    }

    public FixedRateScheduleOptions delay(long value, ChronoUnit timeUnit) {
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
