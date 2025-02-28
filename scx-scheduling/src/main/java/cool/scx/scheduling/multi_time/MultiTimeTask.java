package cool.scx.scheduling.multi_time;

import cool.scx.scheduling.ScheduleTask;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Instant.now;


/// MultipleTimeTask
///
/// @author scx567888
/// @version 0.0.1
public interface MultiTimeTask extends ScheduleTask<MultiTimeTask> {

    MultiTimeTask startTime(Supplier<Instant> startTime);

    default MultiTimeTask startTime(Instant startTime) {
        return startTime(() -> startTime);
    }

    default MultiTimeTask startDelay(Duration delay) {
        return startTime(() -> now().plus(delay));
    }

    MultiTimeTask delay(Duration delay);

    MultiTimeTask executionPolicy(ExecutionPolicy executionPolicy);

}
