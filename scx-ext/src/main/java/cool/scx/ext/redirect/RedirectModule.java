package cool.scx.ext.redirect;

import cool.scx.ansi.Ansi;
import cool.scx.core.Scx;
import cool.scx.core.ScxModule;
import cool.scx.http.helidon.HelidonHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpServer;
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
     * @param port a int
     */
    public static void startRedirect(int port) {
        var router = Router.of();
        router.route().handler(c -> {
            var oldURI = c.request().uri().toString();
            // 4 = "http".length()
            var newURI = "https" + oldURI.substring(4);
            Redirection.ofTemporary(newURI).accept(c);
        });
        var httpServer = new HelidonHttpServer(new HelidonHttpServerOptions().port(port));
        httpServer.requestHandler(router);

        try {
            httpServer.start();
            Ansi.ansi().brightMagenta("转发服务器启动成功 http -> https, 端口号 : " + httpServer.port() + " !!!").println();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "转发服务器启动失败 !!! ", e);
        }
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
