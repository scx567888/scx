package cool.scx.common.test;

import cool.scx.common.lock.LockByKey;
import cool.scx.common.util.$;
import org.testng.annotations.Test;

public class LockByKeyTest {

    private static final LockByKey<Integer> lockByKey = new LockByKey<>();

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        Thread.ofVirtual().start(() -> doSomeThing(1));
        Thread.ofVirtual().start(() -> doSomeThing(2));
        Thread.ofVirtual().start(() -> doSomeThing(3));
        Thread.ofVirtual().start(() -> doSomeThing(4));

        //触发锁
        Thread.ofVirtual().start(() -> doSomeThing(1));
        Thread.ofVirtual().start(() -> doSomeThing(1));
        Thread.ofVirtual().start(() -> doSomeThing(1));
        $.sleep(200);
    }

    public static void doSomeThing(int a) {
        lockByKey.lock(a);
        System.out.println(a + " " + System.currentTimeMillis());
        //延迟释放锁
        $.sleep(10);
        Thread.ofVirtual().start(() -> {
            lockByKey.unlock(a);
        });
    }

}
