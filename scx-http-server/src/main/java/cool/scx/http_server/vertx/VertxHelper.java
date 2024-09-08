package cool.scx.http_server.vertx;

import cool.scx.http_server.ScxHttpServerOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;

import java.util.concurrent.CountDownLatch;

public class VertxHelper {

    public static HttpServerOptions convertOptions(ScxHttpServerOptions options) {
        var vertxOptions = new HttpServerOptions();
        vertxOptions.setPort(options.getPort());
        return vertxOptions;
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> T await(Future<T> future) throws E {
        var countDownLatch = new CountDownLatch(1);
        future.onComplete(ar -> {
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw (E) e;
        }
        if (future.succeeded()) {
            return future.result();
        } else {
            throw (E) future.cause();
        }
    }

}
