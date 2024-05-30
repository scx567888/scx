package cool.scx.common.scheduler;

import java.util.function.Consumer;

/**
 * 固定次数执行的 Runnable
 *
 * @author scx567888
 * @version 1.11.8
 */
final class FixedRunCountRunnable extends CounterRunnable {

    /**
     * 最大执行次数
     */
    private final long maxRunCount;

    public FixedRunCountRunnable(Consumer<ScheduleStatus> scxHandler, long maxRunCount) {
        super(scxHandler);
        this.maxRunCount = maxRunCount;
    }

    @Override
    public void run() {
        super.run();
        if (runCount.get() > maxRunCount) {
            scheduledFuture.cancel(false);
        }
    }

}
