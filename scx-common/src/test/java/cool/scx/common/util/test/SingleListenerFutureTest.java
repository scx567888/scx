package cool.scx.common.util.test;

import cool.scx.common.util.$;
import cool.scx.common.util.SingleListenerFuture;
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

            future.onSuccess(c -> {
                System.out.println(c);
            });

            Thread.startVirtualThread(() -> {
                //在 Future 没有结果的时候 可以 重设结果
                future.onSuccess((c) -> {
                    num.getAndIncrement();
                });
            });

            Thread.startVirtualThread(() -> {
                //延迟 100 毫秒 设置结果
                $.sleep(100);
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
