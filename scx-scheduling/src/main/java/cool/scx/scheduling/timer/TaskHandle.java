package cool.scx.scheduling.timer;

public interface TaskHandle {
    
    /// 取消任务
    boolean cancel();

    /// 状态
    TaskStatus status();
    
}
