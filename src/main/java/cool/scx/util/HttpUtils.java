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
 * <p>HttpUtils class.</p>
 *
 * @author scx567888
 * @version 1.1.9
 */
public final class HttpUtils {

    /**
     * 发送 post 请求
     *
     * @param url     url
     * @param headers 自定义的 headers
     * @param body    发送的信息
     * @return 响应
     * @throws java.io.IOException            if any.
     * @throws java.lang.InterruptedException if any.
     */
    public static HttpResponse<String> post(String url, Map<String, String> headers, Map<String, Object> body) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();
        var bodyBytes = ObjectUtils.writeValueAsString(body, "").getBytes(StandardCharsets.UTF_8);
        var bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(bodyBytes);
        var requestBuilder = HttpRequest.newBuilder(URI.create(url))
                .header(HttpHeaderNames.ACCEPT.toString(), "application/json")
                .header(HttpHeaderNames.CONTENT_TYPE.toString(), "application/json;charset=utf-8");
        headers.forEach(requestBuilder::header);
        var request = requestBuilder.POST(bodyPublisher).build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return httpClient.send(request, bodyHandler);
    }

    /**
     * 向 url 发送请求并获取响应值
     *
     * @param url     url
     * @param headers 自定义 header
     * @return 返回响应
     * @throws java.io.IOException            if any.
     * @throws java.lang.InterruptedException if any.
     */
    public static HttpResponse<String> get(String url, Map<String, String> headers) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();
        var requestBuilder = HttpRequest.newBuilder(URI.create(url))
                .header(HttpHeaderNames.ACCEPT.toString(), "application/json")
                .header(HttpHeaderNames.CONTENT_TYPE.toString(), "application/json;charset=utf-8");
        headers.forEach(requestBuilder::header);
        var request = requestBuilder.GET().build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return httpClient.send(request, bodyHandler);
    }

}
