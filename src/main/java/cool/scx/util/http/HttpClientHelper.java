package cool.scx.util.http;

import cool.scx.util.RandomUtils;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
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

    /**
     * 默认 HTTP_CLIENT 实例
     */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    /**
     * a
     */
    private static final String FORM_BOUNDARY_PREFIX = "----ScxHttpClientHelperFormBoundary";

    /**
     * BODY_HANDLER
     */
    private static final HttpResponse.BodyHandler<String> RESPONSE_BODY_HANDLER = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

    /**
     * a
     *
     * @param url     a
     * @param headers a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(String url, Map<String, String> headers) throws IOException, InterruptedException {
        return delete(HTTP_CLIENT, url, headers);
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
        return delete(HTTP_CLIENT, url, new HashMap<>());
    }

    /**
     * a
     *
     * @param httpClient a
     * @param url        a
     * @param headers    a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(HttpClient httpClient, String url, Map<String, String> headers) throws IOException, InterruptedException {
        return httpClient.send(getRequestBuilder(url, headers).DELETE().build(), RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> delete(HttpClient httpClient, String url) throws IOException, InterruptedException {
        return delete(httpClient, url, new HashMap<>());
    }

    /**
     * a
     *
     * @param url     a
     * @param headers a
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(String url, Map<String, String> headers, String bodyStr) throws IOException, InterruptedException {
        return put(HTTP_CLIENT, url, headers, bodyStr);
    }

    /**
     * a
     *
     * @param url     a
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(String url, String bodyStr) throws IOException, InterruptedException {
        return put(HTTP_CLIENT, url, new HashMap<>(), bodyStr);
    }

    /**
     * a
     *
     * @param httpClient a
     * @param url        a
     * @param headers    a
     * @param bodyStr    a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(HttpClient httpClient, String url, Map<String, String> headers, String bodyStr) throws IOException, InterruptedException {
        return httpClient.send(getRequestBuilder(url, headers).PUT(getBodyPublisher(bodyStr)).build(), RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     *
     * @param url     a
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> put(HttpClient httpClient, String url, String bodyStr) throws IOException, InterruptedException {
        return put(httpClient, url, new HashMap<>(), bodyStr);
    }

    /**
     * a
     *
     * @param url     a
     * @param headers 请求头 若为空则内部会设置为
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, Map<String, String> headers, String bodyStr) throws IOException, InterruptedException {
        return post(HTTP_CLIENT, url, headers, bodyStr);
    }

    /**
     * a
     *
     * @param url     a
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, String bodyStr) throws IOException, InterruptedException {
        return post(HTTP_CLIENT, url, new HashMap<>(), bodyStr);
    }

    /**
     * a
     *
     * @param httpClient a
     * @param url        a
     * @param headers    a
     * @param bodyStr    a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(HttpClient httpClient, String url, Map<String, String> headers, String bodyStr) throws IOException, InterruptedException {
        return httpClient.send(getRequestBuilder(url, headers).POST(getBodyPublisher(bodyStr)).build(), RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     *
     * @param url     a
     * @param bodyStr a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(HttpClient httpClient, String url, String bodyStr) throws IOException, InterruptedException {
        return post(httpClient, url, new HashMap<>(), bodyStr);
    }

    /**
     * a
     *
     * @param httpClient a
     * @param url        a
     * @param headers    a
     * @param formData   a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(HttpClient httpClient, String url, Map<String, String> headers, FormData formData) throws IOException, InterruptedException {
        final String boundary = FORM_BOUNDARY_PREFIX + RandomUtils.getRandomString(8, true);
        headers.put("content-type", "multipart/form-data; boundary=" + boundary);
        return httpClient.send(getRequestBuilder(url, headers).POST(formData.getBodyPublisher(boundary)).build(), RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     *
     * @param httpClient a
     * @param url        a
     * @param formData   a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(HttpClient httpClient, String url, FormData formData) throws IOException, InterruptedException {
        return post(httpClient, url, new HashMap<>(), formData);
    }

    /**
     * a
     *
     * @param url      a
     * @param headers  a
     * @param formData a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, Map<String, String> headers, FormData formData) throws IOException, InterruptedException {
        return post(HTTP_CLIENT, url, headers, formData);
    }

    /**
     * a
     *
     * @param url      a
     * @param formData a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> post(String url, FormData formData) throws IOException, InterruptedException {
        return post(HTTP_CLIENT, url, formData);
    }

    /**
     * a
     *
     * @param url     a
     * @param headers a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(String url, Map<String, String> headers) throws IOException, InterruptedException {
        return get(HTTP_CLIENT, url, headers);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        return get(HTTP_CLIENT, url, new HashMap<>());
    }

    /**
     * a
     *
     * @param url     a
     * @param headers a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(HttpClient httpClient, String url, Map<String, String> headers) throws IOException, InterruptedException {
        return httpClient.send(getRequestBuilder(url, headers).GET().build(), RESPONSE_BODY_HANDLER);
    }

    /**
     * a
     *
     * @param url a
     * @return a
     * @throws IOException          a
     * @throws InterruptedException a
     */
    public static HttpResponse<String> get(HttpClient httpClient, String url) throws IOException, InterruptedException {
        return get(httpClient, url, new HashMap<>());
    }

    /**
     * 获取 HttpRequest.Builder
     *
     * @param url     地址
     * @param headers 头
     * @return r
     */
    private static HttpRequest.Builder getRequestBuilder(String url, Map<String, String> headers) {
        var requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));
        //这里为了移除重复的 header 做一次运算
        var finalHeaders = new HashMap<String, String>();
        headers.forEach((k, v) -> finalHeaders.put(k.trim().toLowerCase(Locale.ROOT), v));
        //如果没有设置 ACCEPT 和 CONTENT_TYPE 这里默认设置为 json
        finalHeaders.putIfAbsent(HttpHeaderNames.ACCEPT.toString().trim().toLowerCase(Locale.ROOT), "application/json");
        finalHeaders.putIfAbsent(HttpHeaderNames.CONTENT_TYPE.toString().trim().toLowerCase(Locale.ROOT), "application/json;charset=utf-8");
        //循环添加头
        finalHeaders.forEach(requestBuilder::setHeader);
        return requestBuilder;
    }

    /**
     * a
     *
     * @param bodyStr a
     * @return a
     */
    private static HttpRequest.BodyPublisher getBodyPublisher(String bodyStr) {
        return HttpRequest.BodyPublishers.ofByteArray(bodyStr.getBytes(StandardCharsets.UTF_8));
    }

}
