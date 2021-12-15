package cool.scx.scheduler;

import cool.scx.ScxHandler;
import cool.scx.ScxHandlerV;
import cool.scx.ScxHandlerVR;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class ScxScheduler {

    private final EventLoopGroup eventLoopGroup;

    public ScxScheduler(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public <T> Future<T> run(ScxHandlerVR<T> task) {
        return eventLoopGroup.submit(task::handle);
    }

    public <T> Future<T> run(ScxHandlerV task, T result) {
        return eventLoopGroup.submit(task::handle, result);
    }

    public Future<?> run(ScxHandlerV scxHandlerV) {
        return eventLoopGroup.submit(scxHandlerV::handle);
    }

    /**
     * 设置计时器
     * <p>
     * 本质上时内部调用 netty 的线程池完成
     * <p>
     * 因为java无法做到特别精确的计时所以此处单位采取 毫秒
     *
     * @param scxHandlerV 执行的事件
     * @param delay       延时执行的时间  单位毫秒
     * @return a
     */
    public ScheduledFuture<?> run(ScxHandlerV scxHandlerV, long delay) {
        return eventLoopGroup.schedule(scxHandlerV::handle, delay, TimeUnit.MILLISECONDS);
    }

    public <R> ScheduledFuture<R> run(ScxHandlerVR<R> scxHandlerVR, long delay) {
        return eventLoopGroup.schedule(scxHandlerVR::handle, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 循环执行一个任务
     * 只有当前任务执行完成后才会执行下一次任务 不会并行执行
     *
     * @param scxHandler 执行的任务
     * @param trigger    触发器
     */
    public void runAtFixedRate(ScxHandler<ScheduleStatus> scxHandler, ScxTrigger trigger) {
        if (trigger.noNeedToRun()) {
            return;
        }
        //查看循环次数 为 -1 则无限循环
        if (trigger.numberOfCycles() == -1) {
            new InfiniteExecutionRunnable(scxHandler).scheduleAtFixedRate(eventLoopGroup, trigger.delay(), trigger.periodValue());
        } else {
            new FixedExecutionRunnable(scxHandler, trigger.numberOfCycles()).scheduleAtFixedRate(eventLoopGroup, trigger.delay(), trigger.periodValue());
        }
    }

    /**
     * 循环执行一个任务
     * 不管当前任务是否执行完成都会直接执行下一次任务 采用并行执行
     *
     * @param scxHandler 执行的任务
     * @param trigger    触发器
     */
    public void runWithFixedDelay(ScxHandler<ScheduleStatus> scxHandler, ScxTrigger trigger) {
        if (trigger.noNeedToRun()) {
            return;
        }
        //查看循环次数 为 -1 则无限循环
        if (trigger.numberOfCycles() == -1) {
            new InfiniteExecutionRunnable(scxHandler).scheduleWithFixedDelay(eventLoopGroup, trigger.delay(), trigger.periodValue());
        } else {
            new FixedExecutionRunnable(scxHandler, trigger.numberOfCycles()).scheduleWithFixedDelay(eventLoopGroup, trigger.delay(), trigger.periodValue());
        }
    }

}
