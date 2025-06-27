package cool.scx.scheduling.timer;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

import java.util.concurrent.TimeUnit;

/// 一个最基本的定时器
public interface Timer {

    <E extends Throwable> TaskHandle<Void, E> runAfter(ScxRunnable<E> action, long delay, TimeUnit unit);

    <V, E extends Throwable> TaskHandle<V, E> runAfter(ScxCallable<V, E> action, long delay, TimeUnit unit);

}
