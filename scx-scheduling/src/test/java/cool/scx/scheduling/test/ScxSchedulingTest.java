package cool.scx.scheduling.test;

import cool.scx.common.util.$;
import cool.scx.scheduling.ScxScheduling;

import java.time.Duration;
import java.time.Instant;

public class ScxSchedulingTest {

    public static void main(String[] args) {
//        test1();
        test2();
        // 因为 ScxVirtualThreadFactory 创建的都是守护线程 所以为了防止程序退出 这里 sleep  一下
        $.sleep(99999);
    }

    public static void test1() {

        //测试单次
        ScxScheduling
                .once()
                .startTime(Instant.now().plusSeconds(1))
                .start((a) -> {
                    System.err.println("这是通过 once() 打印的 !!! runCount : " + a.runCount());
                });

        var status = ScxScheduling
                .once()
                .startTime(Instant.now().plusSeconds(1))
                .start((a) -> {
                    System.err.println("这是通过 once() 打印的 但是会被取消所以不会打印 !!!");
                });

        System.out.println(status.runCount());
        status.cancel();

    }

    public static void test2() {

        //测试 固定频率
        ScxScheduling
                .fixedRate()
                .delay(Duration.ofMillis(1))
                .concurrent(true)
                .maxRunCount(10)
                .start((a) -> {
                    System.err.println("这是通过 fixedRate() 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                });

    }

    public static void test3(String[] args) {

//        scxScheduler.schedule((a) -> {
//            //测试
//            System.err.println("这是使用 Cron 表达式 打印的 : 这是第 " + a.runCount() + " 次执行 !!!");
//        }, ofCron("*/1 * * * * ?"));
//
//        scxScheduler.schedule((a) -> {
//            System.err.println("这是通过 scheduleAtFixedRate() 打印的 : 不限次数 不过到 第 10 次手动取消 , 这是第 " + a.runCount() + " 次执行 !!!");
//            if (a.runCount() >= 10) {
//                a.cancel(false);
//            }
//        }, ofFixedRate().delay(1, SECONDS));
        // 因为 ScxVirtualThreadFactory 创建的都是守护线程 所以为了防止程序退出 这里 sleep 10秒 一下
        $.sleep(10000);
    }

}
