package cool.scx.web;

import cool.scx.base.BaseWebSocketHandler;

public final class ScxWebSocketRoute {

    private String path;
    private BaseWebSocketHandler baseWebSocketHandler;

    public ScxWebSocketRoute() {

    }

    public String path() {
        return path;
    }

    public ScxWebSocketRoute path(String path) {
        this.path = path;
        return this;
    }

    public BaseWebSocketHandler baseWebSocketHandler() {
        return baseWebSocketHandler;
    }

    public ScxWebSocketRoute baseWebSocketHandler(BaseWebSocketHandler baseWebSocketHandler) {
        this.baseWebSocketHandler = baseWebSocketHandler;
        return this;
    }
}
