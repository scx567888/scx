package cool.scx.scheduling;

/// 任务上下文
public interface TaskContext {

    /// 当前运行次数
    long currentRunCount();

    /// 调度上下文
    ScheduleContext context();

    /// 取消调度
    default void cancelSchedule() {
        context().cancel();
    }

}
