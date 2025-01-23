package cool.scx.socket2.test;

import cool.scx.common.util.$;
import cool.scx.http.x.XHttpClient;
import cool.scx.socket2.ScxSocketClient;
import org.testng.annotations.Test;

public class ScxSocketClientTest extends InitLogger {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        //启动服务器
        Thread.ofPlatform().start(()->{
            $.sleep(3000);
            ScxSocketServerTest.test1();    
        });
        

        var webSocketClient = new XHttpClient();

        var scxSocketClient = new ScxSocketClient("ws://127.0.0.1:8990/test", webSocketClient);

        scxSocketClient.onConnect(c -> {

            System.out.println("onOpen");

        });

        scxSocketClient.connect();

    }

}
