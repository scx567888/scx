package cool.scx.http_server;

import java.util.Iterator;
import java.util.List;

public class ScxRoutingContext {

    private final ScxHttpRequest scxHttpRequest;
    protected final Iterator<ScxRoute> iter;

    public ScxRoutingContext(ScxHttpRequest scxHttpRequest, List<ScxRoute> iter) {
        this.scxHttpRequest = scxHttpRequest;
        this.iter = iter.iterator();
    }

    public ScxHttpRequest request() {
        return scxHttpRequest;
    }

    public final void next() {
        while (iter.hasNext()) {
            var next = iter.next();
            if (next.matches(scxHttpRequest)) {
                next.handle(this);
                return;
            }
        }
    }

}
