package cool.scx.scheduling.task.single_time;

import java.time.Instant;
import java.util.function.Supplier;

public interface OnceTask extends SingleTimeTask {

    /**
     * 开始时间 这里采用 Supplier 保证不会因为方法执行的速度导致时间误差
     *
     * @param startTime startTime
     * @return a
     */
    SingleTimeTask startTime(Supplier<Instant> startTime);

    default SingleTimeTask startTime(Instant startTime) {
        return startTime(() -> startTime);
    }

}
