package cool.scx.common.http_client;

import java.net.http.HttpClient;
import java.time.Duration;

public class ScxHttpClientOptions {

    private HttpClient.Version version = HttpClient.Version.HTTP_1_1;

    private Duration connectTimeout;

    public ScxHttpClientOptions connectTimeout(Duration duration) {
        connectTimeout = duration;
        return this;
    }

    public HttpClient.Version version() {
        return version;
    }

    public ScxHttpClientOptions version(HttpClient.Version version) {
        this.version = version;
        return this;
    }

    public HttpClient.Builder toHttpClientBuilder() {
        var builder = HttpClient.newBuilder();
        if (version != null) {
            builder.version(version);
        }
        if (connectTimeout != null) {
            builder.connectTimeout(connectTimeout);
        }
        return builder;
    }

}
