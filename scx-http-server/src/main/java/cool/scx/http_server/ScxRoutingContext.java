package cool.scx.http_server;

import java.util.Iterator;
import java.util.List;

public class ScxRoutingContext {

    protected final Iterator<ScxRoute> iter;
    private final ScxHttpRequest request;

    public ScxRoutingContext(ScxHttpRequest request, List<ScxRoute> iter) {
        this.request = request;
        this.iter = iter.iterator();
    }

    public ScxHttpRequest request() {
        return request;
    }

    public ScxHttpResponse response() {
        return request.response();
    }

    public final void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(request)) {
                next.handle(this);
                return;
            }
        }
    }

}
