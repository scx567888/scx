package cool.scx.scheduling.timer;

import java.util.concurrent.TimeUnit;

/// 一个最基本的定时器
public interface Timer {

    TaskHandle runAfter(Runnable action, long delay, TimeUnit unit);
    
}
