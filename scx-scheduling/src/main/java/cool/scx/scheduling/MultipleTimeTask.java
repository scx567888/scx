package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public interface MultipleTimeTask extends ScheduleTask<MultipleTimeTask> {

    MultipleTimeTask startTime(Supplier<Instant> startTime);

    MultipleTimeTask startTime(Instant startTime);

    MultipleTimeTask delay(Duration delay);

    MultipleTimeTask type(Type type);

    enum Type {
        FIXED_RATE,
        FIXED_DELAY
    }

}
