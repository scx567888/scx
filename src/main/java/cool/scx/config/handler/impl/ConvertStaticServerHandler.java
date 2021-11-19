package cool.scx.config.handler.impl;

import cool.scx.ScxAppRoot;
import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.web.handler.StaticServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * a
 */
public record ConvertStaticServerHandler(
        ScxAppRoot scxAppRoot) implements ScxHandlerR<ScxConfigHandlerParam, List<StaticServer>> {

    @Override
    public List<StaticServer> handle(ScxConfigHandlerParam o) {
        var arrayList = new DefaultValueHandler<>(new ArrayList<Map<String, String>>()).handle(o);
        var tempList = new ArrayList<StaticServer>();
        for (var arg : arrayList) {
            try {
                tempList.add(new StaticServer(arg.get("location"), scxAppRoot.getFileByAppRoot(arg.get("root"))));
            } catch (Exception ignored) {

            }
        }
        return tempList;
    }

}
