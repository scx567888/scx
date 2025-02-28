package cool.scx.scheduling.single_time;

import cool.scx.scheduling.ConcurrencyPolicy;
import cool.scx.scheduling.ScheduleTask;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Instant.now;

/// SingleTimeTask
///
/// @author scx567888
/// @version 0.0.1
public interface SingleTimeTask extends ScheduleTask<SingleTimeTask> {

    SingleTimeTask startTime(Supplier<Instant> startTime);

    default SingleTimeTask startTime(Instant startTime) {
        return startTime(() -> startTime);
    }

    default SingleTimeTask startDelay(Duration delay) {
        return startTime(() -> now().plus(delay));
    }

    @Override
    default SingleTimeTask concurrencyPolicy(ConcurrencyPolicy concurrencyPolicy) {
        // 不支持, 直接跳过
        return this;
    }

    @Override
    default SingleTimeTask maxRunCount(long maxRunCount) {
        // 不支持, 直接跳过
        return this;
    }

}
