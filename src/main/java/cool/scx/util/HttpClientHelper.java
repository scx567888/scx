package cool.scx.util;

import io.netty.handler.codec.http.HttpHeaderNames;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 针对 {@link java.net.http.HttpClient} 进行一些极其简单的封装
 * <p>
 * 如需处理复杂的情况请直接使用  {@link java.net.http.HttpClient}
 *
 * @author scx567888
 * @version 1.1.9
 */
public final class HttpClientHelper {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    public static HttpResponse<String> delete(String url, Map<String, String> headers) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(getNormalRequestBuilder(url, headers).DELETE().build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    public static HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        return delete(url, null);
    }

    public static HttpResponse<String> put(String url, Map<String, String> headers, Object body) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(getNormalRequestBuilder(url, headers).PUT(getBodyPublisher(body)).build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    public static HttpResponse<String> put(String url, Object body) throws IOException, InterruptedException {
        return put(url, null, body);
    }

    public static HttpResponse<String> post(String url, Map<String, String> headers, Object body) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(getNormalRequestBuilder(url, headers).POST(getBodyPublisher(body)).build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    public static HttpResponse<String> post(String url, Object body) throws IOException, InterruptedException {
        return post(url, null, body);
    }

    public static HttpResponse<String> get(String url, Map<String, String> headers) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(getNormalRequestBuilder(url, headers).GET().build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        return get(url, null);
    }

    private static HttpRequest.BodyPublisher getBodyPublisher(Object body) {
        var bodyBytes = ObjectUtils.writeValueAsString(body, "").getBytes(StandardCharsets.UTF_8);
        return HttpRequest.BodyPublishers.ofByteArray(bodyBytes);
    }

    private static HttpRequest.Builder getNormalRequestBuilder(String url, Map<String, String> headers) {
        var requestBuilder = HttpRequest.newBuilder().uri(URI.create(url)).header(HttpHeaderNames.ACCEPT.toString(), "application/json").header(HttpHeaderNames.CONTENT_TYPE.toString(), "application/json;charset=utf-8");
        if (headers != null) {
            headers.forEach(requestBuilder::setHeader);
        }
        return requestBuilder;
    }

}
