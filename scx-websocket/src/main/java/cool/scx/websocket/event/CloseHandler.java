package cool.scx.websocket.event;

public interface CloseHandler {

    void handle(int code, String reason);

}
