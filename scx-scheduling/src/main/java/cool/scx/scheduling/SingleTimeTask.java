package cool.scx.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public interface SingleTimeTask extends ScheduleTask<SingleTimeTask> {

    /**
     * 开始时间 这里采用 Supplier 保证不会因为方法执行的速度导致时间误差
     *
     * @param startTime startTime
     * @return a
     */
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
