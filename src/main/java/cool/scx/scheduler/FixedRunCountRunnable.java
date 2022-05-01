package cool.scx.scheduler;

import cool.scx.ScxHandler;

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

    /**
     * <p>Constructor for FixedRunCountRunnable.</p>
     *
     * @param scxHandler  a {@link ScxHandler} object
     * @param maxRunCount a long
     */
    public FixedRunCountRunnable(ScxHandler<ScheduleStatus> scxHandler, long maxRunCount) {
        super(scxHandler);
        this.maxRunCount = maxRunCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        super.run();
        if (runCount.get() > maxRunCount) {
            scheduledFuture.cancel(false);
        }
    }

}
