package cool.scx.scheduling;

/// 调度状态 这表示宏观的调度的状态而不是 子任务的状态.
/// 比如在两个任务的间歇期间 仍会返回 RUNNING
public enum ScheduleStatus {
    RUNNING,    // 运行中
    DONE,       // 已完成
    CANCELLED,   // 已取消
}
