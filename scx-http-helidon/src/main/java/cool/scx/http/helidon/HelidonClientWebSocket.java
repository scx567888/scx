package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocket;
import io.helidon.websocket.WsSession;

import java.util.concurrent.CountDownLatch;

/**
 * HelidonClientWebSocket
 */
class HelidonClientWebSocket extends HelidonWebSocket implements ScxClientWebSocket {

    private final CountDownLatch lock;

    public HelidonClientWebSocket(CountDownLatch lock) {
        this.lock = lock;
    }

    @Override
    public void onOpen(WsSession session) {
        super.onOpen(session);
        lock.countDown();
    }

}
