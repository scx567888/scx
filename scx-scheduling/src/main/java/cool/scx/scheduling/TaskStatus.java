package cool.scx.scheduling;

public interface TaskStatus {

    /// 当前运行次数
    long currentRunCount();

    /// 调度上下文
    ScheduleContext context();

    default void cancelSchedule() {
        context().cancel();
    }
    
}
