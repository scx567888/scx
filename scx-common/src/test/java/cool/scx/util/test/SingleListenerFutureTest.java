package cool.scx.util.test;

import cool.scx.util.$;
import cool.scx.util.SingleListenerFuture;
import io.vertx.core.Promise;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class SingleListenerFutureTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var num = new AtomicInteger(0);

        for (int i = 0; i < 99999; i++) {

            var promise = Promise.promise();

            var future = new SingleListenerFuture<>(promise.future());
//            var future = promise.future();

            int finalI = i;

            Thread.startVirtualThread(() -> {
                future.onSuccess((c) -> {
                    num.getAndIncrement();
                });
            });

            Thread.startVirtualThread(() -> {
                promise.complete(finalI + " ggg");
            });

        }

        for (int i = 0; i < 10; i++) {
            $.sleep(500);
            //num 最终数量应该是 99999 大于说明重复执行了 小于说明有未执行的
            System.out.println(num);
        }

    }
}
