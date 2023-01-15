package cool.scx.util;

import io.vertx.core.Future;

import java.util.concurrent.CompletableFuture;

/**
 * await {@link io.vertx.core.Future}
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class FutureUtils {

    /**
     * <p>await.</p>
     *
     * @param vertxFuture a {@link io.vertx.core.Future} object
     * @param <T>         a T class
     * @return a T object
     */
    public static <T> T await(Future<T> vertxFuture) {
        var result = new CompletableFuture<T>();
        vertxFuture
                .onSuccess(result::complete)
                .onFailure(result::completeExceptionally);
        return result.join();
    }

}
