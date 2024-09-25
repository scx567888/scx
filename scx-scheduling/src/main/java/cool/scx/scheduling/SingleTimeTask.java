package cool.scx.scheduling;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * 单次任务 只执行一次
 */
public interface SingleTimeTask extends ScheduleTask {

    /**
     * 开始时间 这里采用 Supplier 保证不会因为方法执行的速度导致时间误差
     *
     * @param startTime startTime
     * @return a
     */
    SingleTimeTask startTime(Supplier<Instant> startTime);

    /**
     * 如果 start 方法调用的时候
     * startTime 已经过期(小于当前时间)
     * 则不执行
     *
     * @param skipIfExpired a
     * @return a
     */
    SingleTimeTask skipIfExpired(boolean skipIfExpired);

    default SingleTimeTask startTime(Instant startTime) {
        return startTime(() -> startTime);
    }

}
