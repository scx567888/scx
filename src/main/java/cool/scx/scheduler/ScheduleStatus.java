package cool.scx.scheduler;

import io.netty.util.concurrent.ScheduledFuture;

/**
 * 日程状态 包含循环的次数及 ScheduledFuture
 */
public record ScheduleStatus(long runCount, ScheduledFuture<?> scheduledFuture) {

}
