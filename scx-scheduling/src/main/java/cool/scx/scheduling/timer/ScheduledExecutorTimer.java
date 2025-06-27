package cool.scx.scheduling.timer;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

import java.util.concurrent.*;

/// ScheduledExecutorTimer
///
/// @author scx567888
/// @version 0.0.1
public final class ScheduledExecutorTimer implements Timer {

    private final ScheduledExecutorService executor;

    public ScheduledExecutorTimer(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public <E extends Throwable> TaskHandle<Void, E> runAfter(ScxRunnable<E> action, long delay, TimeUnit unit) {
        var future = executor.schedule(() -> {
            try {
                action.run();
            } catch (Throwable e) {
                throw new WrapperRuntimeException(e);
            }
        }, delay, unit);
        return new TaskHandleImpl<>(future);
    }

    @Override
    public <V, E extends Throwable> TaskHandle<V, E> runAfter(ScxCallable<V, E> action, long delay, TimeUnit unit) {
        var future = executor.schedule(() -> {
            try {
                return action.call();
            } catch (Throwable e) {
                throw new WrapperRuntimeException(e);
            }
        }, delay, unit);
        return new TaskHandleImpl<>(future);
    }

    // 异常包装器
    private static class WrapperRuntimeException extends RuntimeException {
        public WrapperRuntimeException(Throwable cause) {
            super(cause);
        }
    }

    private record TaskHandleImpl<V, E extends Throwable>(ScheduledFuture<?> future) implements TaskHandle<V, E> {

        @Override
        public boolean cancel() {
            return future.cancel(false);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V await() throws E {
            try {
                // 等待任务完成并获取结果
                return (V) future.get(); // 这里会阻塞直到任务完成
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 保留中断状态
                throw new IllegalStateException("Task was interrupted", e);
            } catch (ExecutionException e) {
                // 执行异常，将其转换为原始异常 E
                Throwable cause = e.getCause();
                if (cause instanceof WrapperRuntimeException) {
                    cause = cause.getCause();
                }
                // 抛出原始异常
                throw (E) cause;
            } catch (CancellationException e) {
                throw new IllegalStateException("Task was cancelled", e);
            }
        }

        @Override
        public TaskStatus status() {
            var state = future.state();
            //todo PENDING 如何判断
            return switch (state) {
                case RUNNING -> TaskStatus.RUNNING;
                case SUCCESS -> TaskStatus.SUCCESS;
                case FAILED -> TaskStatus.FAILED;
                case CANCELLED -> TaskStatus.CANCELLED;
            };
        }

        @SuppressWarnings("unchecked")
        @Override
        public V result() {
            return (V) future.resultNow();
        }

        @SuppressWarnings("unchecked")
        @Override
        public E exception() {
            var e = future.exceptionNow();
            if (e instanceof WrapperRuntimeException w) {
                e = w.getCause();
            }
            return (E) e;
        }
        
    }

}
