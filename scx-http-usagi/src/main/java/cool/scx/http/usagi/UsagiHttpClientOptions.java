package cool.scx.http.usagi;

/**
 * ScxHttpClientOptions
 */
public class UsagiHttpClientOptions {

    private Proxy proxy;
    private int bodyBufferSize;

    public UsagiHttpClientOptions() {
        this.proxy = null;
        this.bodyBufferSize = 65536;
    }

    public Proxy proxy() {
        return proxy;
    }

    public UsagiHttpClientOptions proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public int bodyBufferSize() {
        return bodyBufferSize;
    }

    public UsagiHttpClientOptions bodyBufferSize(int bodyBufferSize) {
        this.bodyBufferSize = bodyBufferSize;
        return this;
    }

    public static class Proxy {

        private String host;
        private int port;
        private String username;
        private char[] password;

        public Proxy() {
            this.host = "127.0.0.1";
            this.port = 80;
            this.username = null;
            this.password = null;
        }

        public String host() {
            return host;
        }

        public int port() {
            return port;
        }

        public String username() {
            return username;
        }

        public char[] password() {
            return password;
        }

        public Proxy host(String host) {
            this.host = host;
            return this;
        }

        public Proxy port(int port) {
            this.port = port;
            return this;
        }

        public Proxy username(String username) {
            this.username = username;
            return this;
        }

        public Proxy password(char[] password) {
            this.password = password;
            return this;
        }

    }

}
