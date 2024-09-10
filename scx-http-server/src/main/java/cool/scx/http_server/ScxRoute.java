package cool.scx.http_server;

import java.util.function.Consumer;

public class ScxRoute {

    private Consumer<ScxRoutingContext> handler;

    boolean matches(ScxHttpRequest scxHttpRequest) {
        return true;
    }

    public String path() {
        return null;
    }

    public ScxRoute handler(Consumer<ScxRoutingContext> handler) {
        this.handler = handler;
        return this;
    }

    void handle(ScxRoutingContext next) {
        this.handler.accept(next);
    }

}
