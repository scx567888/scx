package cool.scx.scheduler;

import cool.scx.functional.ScxHandler;
import cool.scx.functional.ScxHandlerA;
import cool.scx.functional.ScxHandlerR;
import io.netty.channel.EventLoopGroup;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 针对 spring 的 ${@link org.springframework.scheduling.TaskScheduler}  进行一些简单的封装
 * <br>
 * 以便可以实现一些简单的任务调度
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxScheduler {

    private final ScheduledExecutorService scheduledExecutorService;

    private final TaskScheduler taskScheduler;

    /**
     * a
     *
     * @param eventLoopGroup a
     */
    public ScxScheduler(EventLoopGroup eventLoopGroup) {
        this.scheduledExecutorService = eventLoopGroup;
        this.taskScheduler = new ConcurrentTaskScheduler(this.scheduledExecutorService);
    }

    /**
     * a
     *
     * @param task a
     * @param <R>  a
     * @return a
     */
    public <R> Future<R> submit(ScxHandlerR<R> task) {
        return scheduledExecutorService.submit(task::handle);
    }

    /**
     * a
     *
     * @param scxHandlerV a
     * @return a
     */
    public Future<?> submit(ScxHandler scxHandlerV) {
        return scheduledExecutorService.submit(scxHandlerV::handle);
    }

    /**
     * a
     *
     * @param scxHandlerVR a
     * @param delay        a
     * @param <R>          a
     * @param unit         a
     * @return a
     */
    public <R> ScheduledFuture<R> schedule(ScxHandlerR<R> scxHandlerVR, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(scxHandlerVR::handle, delay, unit);
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
     * @param unit        a
     * @return a
     */
    public ScheduledFuture<?> schedule(ScxHandler scxHandlerV, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(scxHandlerV::handle, delay, unit);
    }

    /**
     * a
     *
     * @param scxHandler a
     * @param trigger    a
     * @return a
     */
    public ScheduledFuture<?> schedule(ScxHandlerA<ScheduleStatus> scxHandler, Trigger trigger) {
        return new CounterRunnable(scxHandler).schedule(taskScheduler, trigger);
    }

    /**
     * a
     *
     * @param scxHandler a
     * @param startTime  a
     * @param delay      a
     * @return a
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(ScxHandlerA<ScheduleStatus> scxHandler, Instant startTime, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleWithFixedDelay(taskScheduler, startTime, delay);
    }

    /**
     * a
     *
     * @param scxHandler a
     * @param startTime  a
     * @param delay      a
     * @return a
     */
    public ScheduledFuture<?> scheduleAtFixedRate(ScxHandlerA<ScheduleStatus> scxHandler, Instant startTime, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleAtFixedRate(taskScheduler, startTime, delay);
    }

    /**
     * a
     *
     * @param scxHandler  a
     * @param startTime   a
     * @param delay       a
     * @param maxRunCount a
     * @return a
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(ScxHandlerA<ScheduleStatus> scxHandler, Instant startTime, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleWithFixedDelay(taskScheduler, startTime, delay);
    }

    /**
     * a
     *
     * @param scxHandler  a
     * @param startTime   a
     * @param delay       a
     * @param maxRunCount a
     * @return a
     */
    public ScheduledFuture<?> scheduleAtFixedRate(ScxHandlerA<ScheduleStatus> scxHandler, Instant startTime, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleAtFixedRate(taskScheduler, startTime, delay);
    }

    /**
     * a
     *
     * @param scxHandler a
     * @param delay      a
     * @return a
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(ScxHandlerA<ScheduleStatus> scxHandler, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleWithFixedDelay(taskScheduler, delay);
    }

    /**
     * a
     *
     * @param scxHandler a
     * @param delay      a
     * @return a
     */
    public ScheduledFuture<?> scheduleAtFixedRate(ScxHandlerA<ScheduleStatus> scxHandler, Duration delay) {
        return new CounterRunnable(scxHandler).scheduleAtFixedRate(taskScheduler, delay);
    }

    /**
     * a
     *
     * @param scxHandler  a
     * @param delay       a
     * @param maxRunCount a
     * @return a
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(ScxHandlerA<ScheduleStatus> scxHandler, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleWithFixedDelay(taskScheduler, delay);
    }

    /**
     * a
     *
     * @param scxHandler  a
     * @param delay       a
     * @param maxRunCount a
     * @return a
     */
    public ScheduledFuture<?> scheduleAtFixedRate(ScxHandlerA<ScheduleStatus> scxHandler, Duration delay, long maxRunCount) {
        return new FixedRunCountRunnable(scxHandler, maxRunCount).scheduleAtFixedRate(taskScheduler, delay);
    }

}
