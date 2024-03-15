package cool.scx.common.scheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * 日程状态 包含循环的次数 (从 1 开始为第一次) 及 ScheduledFuture 可用于在方法内部中断执行
 */
public record ScheduleStatus(long runCount, ScheduledFuture<?> scheduledFuture) {

}
