package cool.scx.scheduling.task.single_time;

import cool.scx.scheduling.task.Task;

/**
 * 单次任务 只执行一次
 */
public interface SingleTimeTask extends Task {

    /**
     * 如果 start 方法调用的时候
     * startTime 已经过期(小于当前时间)
     * 则不执行
     *
     * @param skipIfExpired a
     * @return a
     */
    SingleTimeTask skipIfExpired(boolean skipIfExpired);

}
