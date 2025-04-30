package cool.scx.websocket.event;

public interface TextMessageHandler {

    void handle(String text, boolean last);

}
