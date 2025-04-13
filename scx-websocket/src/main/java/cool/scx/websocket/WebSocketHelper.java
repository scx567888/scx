package cool.scx.websocket;

import cool.scx.common.util.Base64Utils;
import cool.scx.common.util.HashUtils;

public class WebSocketHelper {

    // 生成 Sec-WebSocket-Accept 的方法
    public static String generateSecWebSocketAccept(String key) {
        // 根据 WebSocket 协议生成接受密钥
        return Base64Utils.encodeToString(HashUtils.sha1(key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"));
    }

}
