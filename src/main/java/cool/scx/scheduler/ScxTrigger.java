package cool.scx.scheduler;

import cool.scx.enumeration.ScxTimeUnit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public final class ScxTrigger {

    /**
     * 循环间隔时间单位
     */
    private final TimeUnit periodTimeUnit;

    /**
     * 循环间隔时间数值
     */
    private final long periodValue;

    /**
     * 循环次数
     */
    private final long numberOfCycles;

    /**
     * 相差时间
     */
    private long delay = 0;

    public ScxTrigger(long periodValue, TimeUnit periodTimeUnit, long numberOfCycles) {
        if (numberOfCycles < 1) {
            throw new IllegalArgumentException("循环次数不能小于 1");
        }
        this.periodTimeUnit = periodTimeUnit;
        this.periodValue = periodValue;
        this.numberOfCycles = numberOfCycles;
    }

    public ScxTrigger(long periodValue, TimeUnit periodTimeUnit) {
        this.periodTimeUnit = periodTimeUnit;
        this.periodValue = periodValue;
        this.numberOfCycles = -1;
    }

    /**
     * 设置第一次启动的时间
     *
     * @param timeUnit t
     * @param index    t
     * @return t
     */
    public ScxTrigger startAt(ScxTimeUnit timeUnit, int index) {
        this.delay = Duration.between(LocalDateTime.now(), timeUnit.getClosestLocalDateTimeFromNow(index)).toMillis();
        return this;
    }

    /**
     * 设置第一次 启动的时间
     *
     * @param startTime a
     * @return a
     */
    public ScxTrigger startAt(LocalDateTime startTime) {
        this.delay = Duration.between(LocalDateTime.now(), startTime).toMillis();
        return this;
    }

    long delay() {
        return delay;
    }

    Long periodValue() {
        return periodTimeUnit.toMillis(periodValue);
    }

    long numberOfCycles() {
        return numberOfCycles;
    }

    /**
     * 检查当前触发器是否有必要运行
     * <p>
     * 不需要运行的情况 : startAt 设置的时间已经过去
     * <p>
     * 比如当前时间 2021/12/15 , startAt 设置 2021/12/14 这时就不需要运行
     * <p>
     *
     * @return r
     */
    boolean noNeedToRun() {
        return delay < 0;
    }

}
