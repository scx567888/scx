package cool.scx.scheduling;

import java.time.Instant;

/// 调度状态
///
/// @author scx567888
/// @version 0.0.1
public interface ScheduleContext {

    /// 任务运行的次数 (有两种情况)
    /// 1, 当作为 task 回调参数时表示当前任务的运行次数(包括当前次数) 不会变化
    /// 2, 当作为 start 返回值时表示当前任务的运行次数 会动态变化
    long runCount();

    /// 下一次运行的时间
    ///  如果下一次不会运行任何任务 则返回 null
    Instant nextRunTime();

    /// 指定次数后运行的时间
    /// 假设 当前调度器拥有 maxRunCount 限制 那么当超出限制之后 会返回 null
    Instant nextRunTime(int count);

    /// 取消任务 (有两种情况)
    /// 1, 当作为 task 回调参数时可用来取消下一次未执行的任务
    /// 2, 当作为 start 返回值时可以取消任何时候的任务 包括任务从未开始也可以取消
    void cancel();

    /// 调度器状态 这表示宏观的调度器的状态而不是 子任务的状态
    /// 比如在两个任务的间歇期间 仍会返回 进行中
    Status status();

    enum Status {
        DONE,        // 已完成
        CANCELED,    // 已取消
    }

}
