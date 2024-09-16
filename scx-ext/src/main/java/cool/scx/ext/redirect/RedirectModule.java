package cool.scx.ext.redirect;

import cool.scx.common.ansi.Ansi;
import cool.scx.core.Scx;
import cool.scx.core.ScxModule;
import cool.scx.http.routing.Route;
import cool.scx.http.routing.Router;
import cool.scx.web.vo.Redirection;

import java.lang.System.Logger;

/**
 * 监听 80 端口并将所有 http 请求重定向 到 https
 *
 * @author scx567888
 * @version 1.15.2
 */
public class RedirectModule extends ScxModule {

    private static final Logger logger = System.getLogger(RedirectModule.class.getName());

    private final int port;

    public RedirectModule() {
        this(80);
    }

    public RedirectModule(int port) {
        this.port = port;
    }

    /**
     * 也可以直接以工具类的形式调用
     *
     * @param vertx a {@link io.vertx.core.Vertx} object
     * @param port  a int
     */
    public static void startRedirect(int port) {
        //todo 
        var router =new Router();
        router.addRoute(Route.of().handler(c -> {
//            var oldURI = c.request().absoluteURI();
            // 4 = "http".length()
//            var newURI = "https" + oldURI.substring(4);
//            Redirection.ofTemporary(newURI).accept(c);
        }));
//        vertx.createHttpServer().requestHandler(router).listen(port, (http) -> {
//            if (http.succeeded()) {
//                Ansi.ansi().brightMagenta("转发服务器启动成功 http -> https, 端口号 : " + port + " !!!").println();
//            } else {
//                logger.log(Logger.Level.ERROR, "转发服务器启动失败 !!! ", http.cause());
//            }
//        });
    }

    @Override
    public String name() {
        return "SCX_EXT-" + super.name();
    }

    @Override
    public void start(Scx scx) {
        //只有当开启 https 的时候才进行转发
        if (scx.scxOptions().isHttpsEnabled()) {
            startRedirect(this.port);
        }
    }

}
