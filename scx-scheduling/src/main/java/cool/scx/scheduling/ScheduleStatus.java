package cool.scx.scheduling;

/**
 * 调度状态
 */
public interface ScheduleStatus {

    /**
     * 任务运行的次数 (有两种情况)
     * 1, 当作为 task 回调参数时表示当前任务的运行次数(包括当前次数) 不会变化
     * 2, 当作为 start 返回值时表示当前任务的运行次数 会动态变化
     *
     * @return c
     */
    long runCount();

    /**
     * 取消任务 (有两种情况)
     * 1, 当作为 task 回调参数时可用来取消下一次未执行的任务
     * 2, 当作为 start 返回值时可以取消任何时候的任务 包括任务从未开始也可以取消
     */
    void cancel();

}
