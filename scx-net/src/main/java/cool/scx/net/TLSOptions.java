package cool.scx.net;

import java.nio.file.Path;

/**
 * TLS 配置
 */
public class TLSOptions {

    private boolean enabled;
    private Path path;
    private String password;

    public TLSOptions() {
        this.enabled = true;
        this.path = null;
        this.password = null;
    }

    public boolean enabled() {
        return enabled;
    }

    public TLSOptions enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Path path() {
        return path;
    }

    public TLSOptions path(Path path) {
        this.path = path;
        return this;
    }

    public String password() {
        return password;
    }

    public TLSOptions password(String password) {
        this.password = password;
        return this;
    }

}
