package cool.scx.tcp;

/// TCP 服务器 配置
///
/// @author scx567888
/// @version 0.0.1
public class TCPServerOptions {

    private int backlog;

    public TCPServerOptions() {
        this.backlog = 128; // 默认背压大小 128
    }

    public TCPServerOptions(TCPServerOptions oldOptions) {
        backlog(oldOptions.backlog());
    }

    public int backlog() {
        return backlog;
    }

    public TCPServerOptions backlog(int backlog) {
        this.backlog = backlog;
        return this;
    }

}

