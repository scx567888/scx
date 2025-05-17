package cool.scx.websocket.x.test;

import cool.scx.websocket.x.ScxWebSocketClientHelper;

import java.io.IOException;

public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        XTest.test1();
        var webSocket = ScxWebSocketClientHelper.webSocketHandshakeRequest()
                .uri("http://localhost:8899/中文路径😎😎😎😎?a=1&b=llll")
                .addHeader("a", "b")
                .webSocket();

        System.out.println("连接成功");
        webSocket.send("测试数据");

    }

}

