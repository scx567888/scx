package cool.scx.scheduler;

import cool.scx.functional.ScxHandler;

/**
 * 固定次数执行的 Runnable
 */
final class FixedRunCountRunnable extends CounterRunnable {

    /**
     * 最大执行次数
     */
    private final long maxRunCount;

    public FixedRunCountRunnable(ScxHandler<ScheduleStatus> scxHandler, long maxRunCount) {
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