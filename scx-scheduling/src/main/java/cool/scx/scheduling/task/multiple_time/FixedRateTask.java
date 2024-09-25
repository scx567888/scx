package cool.scx.scheduling.task.multiple_time;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public interface FixedRateTask extends MultipleTimeTask {

    /**
     * 开始时间 这里采用 Supplier 保证不会因为方法执行的速度导致时间误差
     *
     * @param startTime startTime
     * @return a
     */
    FixedRateTask startTime(Supplier<Instant> startTime);

    FixedRateTask delay(Duration delay);

    default FixedRateTask startTime(Instant startTime) {
        return startTime(() -> startTime);
    }

}
