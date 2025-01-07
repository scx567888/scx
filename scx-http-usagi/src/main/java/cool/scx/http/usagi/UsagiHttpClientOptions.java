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

    private int bodyBufferSize;//todo 暂时未用到
    private TCPClientType tcpClientType;

    public UsagiHttpClientOptions() {
        this.bodyBufferSize = 1024 * 64;// 默认 64 KB
        this.tcpClientType = TCPClientType.CLASSIC;
    }

    public UsagiHttpClientOptions(UsagiHttpClientOptions oldOptions) {
        super(oldOptions);
        bodyBufferSize(oldOptions.bodyBufferSize());
        tcpClientType(oldOptions.tcpClientType());
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

    @Override
    public UsagiHttpClientOptions tls(TLS tls) {
        super.tls(tls);
        return this;
    }

    @Override
    public UsagiHttpClientOptions proxy(Proxy proxy) {
        super.proxy(proxy);
        return this;
    }

    public enum TCPClientType {
        CLASSIC,
        NIO
    }

}
