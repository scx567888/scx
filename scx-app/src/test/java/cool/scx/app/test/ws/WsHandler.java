package cool.scx.app.test.ws;

import cool.scx.common.constant.DateTimeFormatters;
import dev.scx.scheduling.ScxScheduling;
import cool.scx.web.BaseWebSocketHandler;
import cool.scx.web.annotation.ScxWebSocketRoute;
import cool.scx.websocket.ScxServerWebSocketHandshakeRequest;
import cool.scx.websocket.event.ScxEventWebSocket;

import java.time.LocalDateTime;

@ScxWebSocketRoute("/now")
public class WsHandler implements BaseWebSocketHandler {


    @Override
    public void onHandshakeRequest(ScxServerWebSocketHandshakeRequest request) throws Exception {
        var context = ScxEventWebSocket.of(request.webSocket());
        System.out.println("连接了");
        var scheduleContext = ScxScheduling.setInterval(() -> {
            System.out.println("发送消息");
            context.send(LocalDateTime.now().format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss_SSS));
        }, 500);
        context.onTextMessage((c, b) -> {
            System.out.println("收到消息 : " + c + " " + b);
        });
        context.onClose((c, b) -> {
            System.out.println("Close " + c + " " + b);
            scheduleContext.cancel();
        });
        context.onError(error -> {
            System.out.println("Error " + error);
            scheduleContext.cancel();
        });
        context.start();
    }
}
