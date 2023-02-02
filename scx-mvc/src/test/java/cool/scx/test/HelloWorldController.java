package cool.scx.test;

import cool.scx.mvc.annotation.ScxMapping;
import cool.scx.mvc.exception.ForbiddenException;
import cool.scx.mvc.vo.DataJson;

import java.util.Map;

@ScxMapping
public class HelloWorldController {

    @ScxMapping("hello")
    public Object hello() {
        return DataJson.ok().data(Map.of("name", "scx567888😁"));
    }

    @ScxMapping("no-perm")
    public Object noPerm() {
        throw new ForbiddenException();
    }

}
