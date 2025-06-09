package cool.scx.http.x.test;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.http.x.HttpServer;
import cool.scx.http.x.HttpServerOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import static cool.scx.http.headers.HttpFieldName.ACCESS_CONTROL_ALLOW_ORIGIN;

public class bbb {
    public static void main(String[] args) throws IOException {
        var s=new HttpServer(new HttpServerOptions().maxPayloadSize(Integer.MAX_VALUE));
        s.onRequest(c->{
            c.response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            System.out.println("收到请求");
            try {
                MultiPart multiPart = c.body().asMultiPart();
                for (MultiPartPart multiPartPart : multiPart) {
                    multiPartPart.asPath(Path.of("C:\\Users\\scx\\Downloads\\Fbas-Files-master"+ RandomUtils.randomString(8) +".zip"));
                    System.out.println(multiPartPart.name());
                }    
            }catch (Exception e){
                e.printStackTrace();
            }
            
            c.response().send("ok");
        });
        s.start(8080);
    }
}
