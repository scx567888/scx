package cool.scx.websocket.handler;

public interface TextMessageHandler {

    void handle(String text, boolean last);

}
