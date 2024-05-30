package cool.scx.common.scheduler.test;

import cool.scx.common.scheduler.ScxScheduler;
import cool.scx.common.util.$;
import cool.scx.common.util.ScxVirtualThreadFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.Executors.newScheduledThreadPool;

public class ScxSchedulerTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        var scxScheduler = new ScxScheduler(newScheduledThreadPool(Integer.MAX_VALUE, new ScxVirtualThreadFactory()));
        AtomicLong l = new AtomicLong();
        try (scxScheduler) {
            for (int i = 0; i <= 99999; i++) {
                int finalI = i;
                scxScheduler.execute(() -> {
                    $.sleep(1000);
                    l.addAndGet(finalI);
                });
            }
        }
        Assert.assertEquals(l.get(), 4999950000L);
    }

    public static void test2() {
        var scxScheduler = new ScxScheduler(newScheduledThreadPool(Integer.MAX_VALUE, new ScxVirtualThreadFactory()));
        //测试定时任务
        scxScheduler.scheduleAtFixedRate((a) -> {
            //测试
            System.err.println("这是通过 scheduleAtFixedRate() 打印的 : 一共 10 次 , 这时第 " + a.runCount() + " 次执行 !!!");
        }, Instant.now().plusSeconds(3), Duration.of(1, ChronoUnit.SECONDS), 10);

        scxScheduler.schedule((a) -> {
            //测试
            System.err.println("这是使用 Cron 表达式 打印的 : 这是第 " + a.runCount() + " 次执行 !!!");
        }, new CronTrigger("*/1 * * * * ?"));

        scxScheduler.scheduleAtFixedRate((a) -> {
            System.err.println("这是通过 scheduleAtFixedRate() 打印的 : 不限次数 不过到 第 10 次手动取消 , 这是第 " + a.runCount() + " 次执行 !!!");
            if (a.runCount() >= 10) {
                a.scheduledFuture().cancel(false);
            }
        }, Instant.now().plusSeconds(3), Duration.of(1, ChronoUnit.SECONDS));
        // 因为 ScxVirtualThreadFactory 创建的都是守护线程 所以为了防止程序退出 这里 sleep 10秒 一下
        $.sleep(10000);
    }

}
