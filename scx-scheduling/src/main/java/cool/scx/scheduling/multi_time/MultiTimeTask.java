package cool.scx.scheduling.multi_time;

import cool.scx.scheduling.ScheduleTask;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;


/// MultipleTimeTask
///
/// @author scx567888
/// @version 0.0.1
public interface MultiTimeTask extends ScheduleTask {

    MultiTimeTask startTime(Supplier<Instant> startTime);

    MultiTimeTask startTime(Instant startTime);

    MultiTimeTask delay(Duration delay);

    MultiTimeTask type(Type type);

    enum Type {
        FIXED_RATE,
        FIXED_DELAY
    }

}
