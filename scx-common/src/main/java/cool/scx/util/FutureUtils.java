package cool.scx.util;

import io.vertx.core.Future;

import java.util.concurrent.CompletableFuture;

/**
 * await {@link Future}
 */
public final class FutureUtils {

    public static <T> T await(Future<T> vertxFuture) {
        var result = new CompletableFuture<T>();
        vertxFuture
                .onSuccess(result::complete)
                .onFailure(result::completeExceptionally);
        return result.join();
    }

}
