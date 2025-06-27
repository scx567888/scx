package cool.scx.scheduling.timer;

/// TaskHandle
///
/// @author scx567888
/// @version 0.0.1
public interface TaskHandle<V, E extends Throwable> {

    /// 取消任务
    boolean cancel();

    /// 状态
    TaskStatus status();

    /// 获取 结果, 仅在任务执行成功后可用
    ///
    /// @throws IllegalStateException 任务状态异常, 如任务 未执行,已取消 或者 已失败
    V result() throws IllegalStateException;

    /// 获取 异常, 仅在任务执行失败后可用
    ///
    /// @throws IllegalStateException 任务状态异常, 如任务 未执行,已取消 或者 已成功
    E exception() throws IllegalStateException;

}
