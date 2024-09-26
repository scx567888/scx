package cool.scx.scheduling.test;

import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScxScheduling;

import java.time.Duration;
import java.time.Instant;

public class ScxSchedulingTest {

    public static void main(String[] args) throws InterruptedException {
//        test1();
//        test2();
        test3();
    }

    public static void test1() {

        //测试单次

        //正常执行
        ScxScheduling
                .once()
                .startTime(Instant.now().plusSeconds(1))
                .start((a) -> {
                    System.err.println("这是通过 once() 0 打印的 !!! runCount : " + a.runCount());
                });

        //过期执行 1
        ScxScheduling
                .once()
                .startTime(Instant.now().minusSeconds(1))
                .expirationPolicy(ExpirationPolicy.BACKTRACKING_IGNORE)
                .start((a) -> {
                    System.err.println("这是通过 once() 1 打印的 !!! runCount : " + a.runCount());
                });

        //过期执行 2
        ScxScheduling
                .once()
                .startTime(Instant.now().minusSeconds(1))
                .expirationPolicy(ExpirationPolicy.BACKTRACKING_COMPENSATION)
                .start((a) -> {
                    System.err.println("这是通过 once() 2 打印的 !!! runCount : " + a.runCount());
                });

        //测试取消
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

        //正常执行
        ScxScheduling
                .fixedRate()
                .delay(Duration.ofMillis(200))
                .concurrent(true)
                .maxRunCount(10)
                .task((a) -> {
                    System.err.println("这是通过 fixedRate() 0 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                })
//                .start()
        ;

//        //过期策略 1
        ScxScheduling
                .fixedRate()
                .startTime(Instant.now().minusMillis(10000))
                .expirationPolicy(ExpirationPolicy.IMMEDIATE_IGNORE)
                .delay(Duration.ofMillis(500))
                .concurrent(true)
                .maxRunCount(10)
                .task((a) -> {
                    System.err.println("这是通过 fixedRate() 1 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                })
//                .start()
        ;

        //过期策略 2
        ScxScheduling
                .fixedRate()
                .startTime(Instant.now().minusMillis(1000))
                .expirationPolicy(ExpirationPolicy.BACKTRACKING_IGNORE)
                .delay(Duration.ofMillis(500))
                .concurrent(true)
                .maxRunCount(10)
                .task((a) -> {
                    System.err.println("这是通过 fixedRate() 2 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                })
//                .start()
        ;

        //过期策略 3
        ScxScheduling
                .fixedRate()
                .startTime(Instant.now().minusMillis(1000))
                .expirationPolicy(ExpirationPolicy.IMMEDIATE_COMPENSATION)
                .delay(Duration.ofMillis(500))
                .concurrent(true)
                .maxRunCount(10)
                .task((a) -> {
                    System.err.println("这是通过 fixedRate() 3 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                })
//                .start()
        ;

        //过期策略 3
        ScxScheduling
                .fixedRate()
                .startTime(Instant.now().minusMillis(1000))
                .expirationPolicy(ExpirationPolicy.BACKTRACKING_COMPENSATION)
                .delay(Duration.ofMillis(500))
                .concurrent(true)
                .maxRunCount(10)
                .task((a) -> {
                    System.err.println("这是通过 fixedRate() 3 打印的 : 第 10 次会取消 , 这时第 " + a.runCount() + " 次执行 !!!");
                })
                .start()
        ;

    }

    public static void test3() {

        ScxScheduling
                .cron()
                .expression("*/1 * * * * ?")
                .concurrent(true)
                .maxRunCount(3)
                .start((a) -> {
                    //测试
                    System.err.println("这是使用 Cron 表达式 打印的 : 这是第 " + a.runCount() + " 次执行 !!!");
                });

    }

}
