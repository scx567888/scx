package cool.scx.http.helidon;

import io.helidon.webclient.api.Proxy;

/**
 * HelidonHttpClientOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HelidonHttpClientOptions {

    private Proxy proxy;
    private int bodyBufferSize;

    public HelidonHttpClientOptions() {
        this.proxy = Proxy.noProxy();
        this.bodyBufferSize = 65536;
    }

    public Proxy proxy() {
        return proxy;
    }

    public HelidonHttpClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public HelidonHttpClientOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

}
