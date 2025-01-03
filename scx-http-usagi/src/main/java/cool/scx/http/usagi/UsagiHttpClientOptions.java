package cool.scx.http.usagi;

import cool.scx.tcp.ScxTCPClientOptions;
import cool.scx.tcp.proxy.Proxy;
import cool.scx.tcp.tls.TLS;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClientOptions extends ScxTCPClientOptions {

    private TCPClientType tcpClientType;
    private int bodyBufferSize;

    public UsagiHttpClientOptions() {
        this.bodyBufferSize = 65536;
        this.tcpClientType = TCPClientType.NIO;
    }

    @Override
    public UsagiHttpClientOptions proxy(Proxy proxy) {
        super.proxy(proxy);
        return this;

    }

    @Override
    public UsagiHttpClientOptions tls(TLS tls) {
        super.tls(tls);
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public UsagiHttpClientOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

    public TCPClientType tcpClientType() {
        return tcpClientType;
    }

    public UsagiHttpClientOptions tcpClientType(TCPClientType tcpClientType) {
        this.tcpClientType = tcpClientType;
        return this;
    }

    public enum TCPClientType {
        CLASSIC,
        NIO
    }

}
