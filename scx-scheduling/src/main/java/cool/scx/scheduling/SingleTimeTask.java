package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/// SingleTimeTask
///
/// @author scx567888
/// @version 0.0.1
public interface SingleTimeTask extends ScheduleTask {

    SingleTimeTask startTime(Supplier<Instant> startTime);

    SingleTimeTask startTime(Instant startTime);

    SingleTimeTask startDelay(Duration delay);

    @Override
    default SingleTimeTask concurrent(boolean concurrent) {
        //不支持所以直接跳过
        return this;
    }

    @Override
    default SingleTimeTask maxRunCount(long maxRunCount) {
        //不支持所以直接跳过
        return this;
    }

}
