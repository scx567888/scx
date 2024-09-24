package cool.scx.scheduling;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public non-sealed class OnceScheduleOptions implements ScheduleOptions {

    private Instant startTime;

    public OnceScheduleOptions() {
        this.startTime = Instant.now();
    }

    public OnceScheduleOptions startTime(LocalDateTime localDateTime) {
        this.startTime = localDateTime.toInstant(ZoneOffset.ofHours(8));
        return this;
    }

    public OnceScheduleOptions startTime(long value, ChronoUnit timeUnit) {
        this.startTime = Instant.now().plus(value, timeUnit);
        return this;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

}
