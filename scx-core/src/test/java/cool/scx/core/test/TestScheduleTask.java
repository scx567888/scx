package cool.scx.core.test;

import cool.scx.core.annotation.ScxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

@ScxService
public class TestScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(TestScheduleTask.class);

    @Scheduled(cron = "*/1 * * * * ?")
    public void oneSecondTasks() {
        logger.error("这是 通过注解的 定时任务打印的 !!!");
    }

}
