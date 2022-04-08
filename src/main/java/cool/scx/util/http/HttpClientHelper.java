package cool.scx.util.http;

import cool.scx.enumeration.HttpMethod;
import cool.scx.util.URIBuilder;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * 针对 {@link java.net.http.HttpClient} 进行一些极其简单的封装
 * <p>
 * 如需处理复杂的情况请直接使用  {@link java.net.http.HttpClient}
 *
 * @author scx567888
 * @version 1.1.9
 */
public final class HttpClientHelper {

    /**
     * 默认的 HTTP_CLIENT 实例
     */
    public static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newBuilder().build();

    /**
     * 默认的 RESPONSE_BODY_HANDLER
     */
    private static final HttpResponse.BodyHandler<String> DEFAULT_RESPONSE_BODY_HANDLER = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        return get(url, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(String url, Options options) throws IOException, InterruptedException {
        return get(new URIBuilder(url), options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(URIBuilder url) throws IOException, InterruptedException {
        return get(url, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(URIBuilder url, Options options) throws IOException, InterruptedException {
        return request(url, HttpMethod.GET, null, options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url) throws IOException, InterruptedException {
        return post(url, null);
    }

    /**
     * a
     *
     * @param url  a
     * @param body a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, Body body) throws IOException, InterruptedException {
        return post(url, body, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param body    a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, Body body, Options options) throws IOException, InterruptedException {
        return post(new URIBuilder(url), body, options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(URIBuilder url) throws IOException, InterruptedException {
        return post(url, null);
    }

    /**
     * a
     *
     * @param url  a
     * @param body a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(URIBuilder url, Body body) throws IOException, InterruptedException {
        return post(url, body, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param body    a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(URIBuilder url, Body body, Options options) throws IOException, InterruptedException {
        return request(url, HttpMethod.POST, body, options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        return delete(url, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(String url, Options options) throws IOException, InterruptedException {
        return delete(new URIBuilder(url), options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(URIBuilder url) throws IOException, InterruptedException {
        return delete(url, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(URIBuilder url, Options options) throws IOException, InterruptedException {
        return request(url, HttpMethod.DELETE, null, options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(String url) throws IOException, InterruptedException {
        return put(url, null);
    }

    /**
     * a
     *
     * @param url  a
     * @param body a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(String url, Body body) throws IOException, InterruptedException {
        return put(url, body, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param body    a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(String url, Body body, Options options) throws IOException, InterruptedException {
        return put(new URIBuilder(url), body, options);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(URIBuilder url) throws IOException, InterruptedException {
        return put(url, null);
    }

    /**
     * a
     *
     * @param url  a
     * @param body a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(URIBuilder url, Body body) throws IOException, InterruptedException {
        return put(url, body, null);
    }

    /**
     * a
     *
     * @param url     a
     * @param body    a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(URIBuilder url, Body body, Options options) throws IOException, InterruptedException {
        return request(url, HttpMethod.PUT, body, options);
    }

    /**
     * a
     *
     * @param url     a
     * @param method  a
     * @param body    a
     * @param options a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    private static HttpResponse<String> request(URIBuilder url, HttpMethod method, Body body, Options options) throws IOException, InterruptedException {
        var b = body != null ? body : new EmptyBody();
        var o = options != null ? options : new Options();
        var httpClient = o.httpClient() != null ? o.httpClient() : DEFAULT_HTTP_CLIENT;
        var httpRequestBuilder = o.getHttpRequestBuilder().uri(url.toURI());
        var bodyPublisher = b.getBodyPublisher(httpRequestBuilder);
        var httpRequest = switch (method) {
            case GET -> httpRequestBuilder.GET().build();
            case POST -> httpRequestBuilder.POST(bodyPublisher).build();
            case DELETE -> httpRequestBuilder.DELETE().build();
            case PUT -> httpRequestBuilder.PUT(bodyPublisher).build();
            default -> throw new IllegalArgumentException("method 只能为 [GET, POST, DELETE, PUT] 这四种 !!! method : " + method);
        };
        return httpClient.send(httpRequest, DEFAULT_RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     */
    public interface Body {

        /**
         * a
         *
         * @param builder a
         * @return a
         */
        HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder);

    }

    /**
     * a
     */
    public static class Options {

        private final HttpRequest.Builder builder = HttpRequest.newBuilder();

        private HttpClient httpClient;

        public Options httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Options header(String name, String value) {
            builder.header(name, value);
            return this;
        }

        public Options setHeader(String name, String value) {
            builder.setHeader(name, value);
            return this;
        }

        public HttpRequest.Builder getHttpRequestBuilder() {
            return builder;
        }

        public HttpClient httpClient() {
            return httpClient;
        }

    }

}
