package cool.scx.websocket.handler;

public interface BinaryMessageHandler {

    void handle(byte[] binary, boolean last);

}
