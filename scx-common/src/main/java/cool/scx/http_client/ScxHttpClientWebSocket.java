package cool.scx.http_client;

import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxRunnable;

public interface ScxHttpClientWebSocket {

    void binaryMessageHandler(ScxConsumer<byte[], ?> handler);

    void closeHandler(ScxRunnable<?> handler);

    void textMessageHandler(ScxConsumer<String, ?> handler);

    void exceptionHandler(ScxConsumer<Throwable, ?> handler);

}
