package cool.scx.tcp.proxy;

/// 代理
/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class Proxy {

    private boolean enabled;

    public Proxy() {
        this.enabled = true;
    }

    public boolean enabled() {
        return enabled;
    }

    public Proxy enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

}
