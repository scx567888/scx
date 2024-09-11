package cool.scx.http;

import java.util.Iterator;
import java.util.List;

public class ScxRoutingContext {

    protected final Iterator<ScxRoute> iter;
    private final ScxHttpServerRequest request;

    public ScxRoutingContext(ScxHttpServerRequest request, List<ScxRoute> iter) {
        this.request = request;
        this.iter = iter.iterator();
    }

    public ScxHttpServerRequest request() {
        return request;
    }

    public ScxHttpServerResponse response() {
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
