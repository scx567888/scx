package cool.scx.scheduling.task.single_time;

import java.time.Duration;

public interface DelayTask extends SingleTimeTask {

    /**
     * 开始延时
     *
     * @param delay delay
     * @return delay
     */
    DelayTask startDelay(Duration delay);

}
