package cool.scx.test;

import cool.scx.mvc.annotation.ScxRoute;
import cool.scx.mvc.exception.ForbiddenException;
import cool.scx.mvc.vo.Data;

import java.util.Map;

@ScxRoute
public class HelloWorldController {

    @ScxRoute("hello")
    public Object hello() {
        return Data.ok().put("name", "scx567888😁");
    }

    @ScxRoute("no-perm")
    public Object noPerm() {
        throw new ForbiddenException();
    }

}
