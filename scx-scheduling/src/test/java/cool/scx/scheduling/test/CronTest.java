package cool.scx.scheduling.test;

import cool.scx.scheduling.cron.Cron;

public class CronTest {
    
    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        Cron cron = Cron.of("1-1/2 * * * * *");
        System.out.println(cron);
    }
    
}
