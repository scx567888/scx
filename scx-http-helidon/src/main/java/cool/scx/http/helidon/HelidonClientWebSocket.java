package cool.scx.http.helidon;

import cool.scx.http.ScxClientWebSocket;
import cool.scx.http.ScxServerWebSocket;
import io.helidon.common.buffers.BufferData;
import io.helidon.http.Headers;
import io.helidon.http.HttpPrologue;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsSession;
import io.helidon.websocket.WsUpgradeException;

import java.util.Optional;
import java.util.function.Consumer;

//todo 
public class HelidonClientWebSocket implements ScxClientWebSocket, WsListener {

    @Override
    public ScxServerWebSocket onTextMessage(Consumer<String> textMessageHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket onPing(Consumer<byte[]> pingHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket onPong(Consumer<byte[]> pongHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket onClose(Consumer<Integer> closeHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket onError(Consumer<Throwable> errorHandler) {
        return null;
    }

    @Override
    public ScxServerWebSocket send(String textMessage, boolean var2) {
        return null;
    }

    @Override
    public ScxServerWebSocket send(byte[] binaryMessage, boolean var2) {
        return null;
    }

    @Override
    public ScxServerWebSocket ping(byte[] data) {
        return null;
    }

    @Override
    public ScxServerWebSocket pong(byte[] data) {
        return null;
    }

    @Override
    public ScxServerWebSocket close(int var1, String var2) {
        return null;
    }

    //*************** 方法 *************

    @Override
    public void onMessage(WsSession session, String text, boolean last) {
        WsListener.super.onMessage(session, text, last);
    }

    @Override
    public void onMessage(WsSession session, BufferData buffer, boolean last) {
        WsListener.super.onMessage(session, buffer, last);
    }

    @Override
    public void onPing(WsSession session, BufferData buffer) {
        WsListener.super.onPing(session, buffer);
    }

    @Override
    public void onPong(WsSession session, BufferData buffer) {
        WsListener.super.onPong(session, buffer);
    }

    @Override
    public void onClose(WsSession session, int status, String reason) {
        WsListener.super.onClose(session, status, reason);
    }

    @Override
    public void onError(WsSession session, Throwable t) {
        WsListener.super.onError(session, t);
    }

    @Override
    public void onOpen(WsSession session) {
        WsListener.super.onOpen(session);
    }

    @Override
    public Optional<Headers> onHttpUpgrade(HttpPrologue prologue, Headers headers) throws WsUpgradeException {
        return WsListener.super.onHttpUpgrade(prologue, headers);
    }

}
