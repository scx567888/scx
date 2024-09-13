package cool.scx.web.test;

import cool.scx.web.annotation.ScxRoute;
import cool.scx.http.exception.ForbiddenException;
import cool.scx.web.vo.Result;

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
