package cool.scx.websocket.event;

public interface BinaryMessageHandler {

    void handle(byte[] binary, boolean last);

}
