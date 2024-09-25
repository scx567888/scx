package cool.scx.scheduling.task.single_time;

public interface DelayTask extends SingleTimeTask {

    /**
     * 延时时间
     *
     * @param delay delay
     * @return delay
     */
    DelayTask delay(long delay);

}
