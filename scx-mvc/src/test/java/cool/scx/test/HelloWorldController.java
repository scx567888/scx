package cool.scx.test;

import cool.scx.mvc.annotation.ScxRoute;
import cool.scx.mvc.exception.ForbiddenException;
import cool.scx.mvc.vo.Result;

@ScxRoute
public class HelloWorldController {

    @ScxRoute("hello")
    public Object hello() {
        return Result.ok().put("name", "scx567888😁");
    }

    @ScxRoute("no-perm")
    public Object noPerm() {
        throw new ForbiddenException();
    }

}
