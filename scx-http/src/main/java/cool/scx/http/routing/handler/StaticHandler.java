package cool.scx.http.routing.handler;

import cool.scx.http.exception.NotFoundException;
import cool.scx.http.routing.RoutingContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static cool.scx.http.HttpMethod.GET;
import static cool.scx.http.HttpMethod.HEAD;
import static cool.scx.http.routing.handler.StaticHelper.sendStatic;

public class StaticHandler implements Consumer<RoutingContext> {

    private final Path root;

    public StaticHandler(Path root) {
        this.root = root;
    }

    @Override
    public void accept(RoutingContext routingContext) {
        var request = routingContext.request();

        if (request.method() != GET && request.method() != HEAD) {
            routingContext.next();
            return;
        }

        var p = routingContext.pathParams().get("*");
        var filePath = getFilePath(p);
        var notExists = Files.notExists(filePath);
        //文件不存在 
        if (notExists) {
            routingContext.next();
            return;
        }

        sendStatic(filePath, routingContext);

    }

    public Path getFilePath(String p) {
        //我们需要支持单文件形式 既无论请求路径为什么都返回单文件
        var regularFile = Files.isRegularFile(root);
        if (regularFile) {
            return root;
        }
        var path = root.resolve(p).normalize();
        // 1, 重要!!! , 防止访问到上级文件的情况
        if (!path.startsWith(root)) {
            throw new NotFoundException();
        }
        if (Files.isDirectory(path)) {
            return path.resolve("index.html");
        } else {
            return path;
        }
    }

}
