package cool.scx.web;

import cool.scx.base.BaseWebSocketHandler;

/**
 * a
 */
public final class ScxWebSocketRoute {

    private String path;

    private BaseWebSocketHandler baseWebSocketHandler;

    /**
     * a
     */
    public ScxWebSocketRoute() {

    }

    /**
     * a
     *
     * @return a
     */
    public String path() {
        return path;
    }

    /**
     * a
     *
     * @param path a
     * @return a
     */
    public ScxWebSocketRoute path(String path) {
        this.path = path;
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public BaseWebSocketHandler baseWebSocketHandler() {
        return baseWebSocketHandler;
    }

    /**
     * a
     *
     * @param baseWebSocketHandler a
     * @return a
     */
    public ScxWebSocketRoute baseWebSocketHandler(BaseWebSocketHandler baseWebSocketHandler) {
        this.baseWebSocketHandler = baseWebSocketHandler;
        return this;
    }

}
