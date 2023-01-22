package cool.scx.http_client.body;

import cool.scx.http_client.ScxHttpClientRequestBody;
import cool.scx.http_client.body.form_data.FormDataItem;
import cool.scx.http_client.body.form_data.FormDataIterable;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.http_client.body.form_data.FormDataHelper.initEncoder;
import static cool.scx.util.ScxExceptionHelper.wrap;

/**
 * 使用 netty 的 HttpPostRequestEncoder 简化 FormData 的创建
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class FormData implements ScxHttpClientRequestBody {

    private final List<FormDataItem> items = new ArrayList<>();

    /**
     * <p>attribute.</p>
     *
     * @param name a {@link String} object
     * @param text a {@link Object} object
     * @return a {@link FormData} object
     */
    public FormData attribute(String name, Object text) {
        this.items.add(new FormDataItem(name, text.toString()));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link String} object
     * @param fileByte an array of {@link byte} objects
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte) {
        this.items.add(new FormDataItem(name, fileByte));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link String} object
     * @param fileByte an array of {@link byte} objects
     * @param filename a {@link String} object
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte, String filename) {
        this.items.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name        a {@link String} object
     * @param fileByte    an array of {@link byte} objects
     * @param filename    a {@link String} object
     * @param contentType a {@link String} object
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte, String filename, String contentType) {
        this.items.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link String} object
     * @param filePath a {@link Path} object
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, Path filePath) {
        this.items.add(new FormDataItem(name, filePath));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link String} object
     * @param filePath a {@link Path} object
     * @param filename a {@link String} object
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, Path filePath, String filename) {
        this.items.add(new FormDataItem(name, filePath, filename));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name        a {@link String} object
     * @param filePath    a {@link Path} object
     * @param filename    a {@link String} object
     * @param contentType a {@link String} object
     * @return a {@link FormData} object
     */
    public FormData fileUpload(String name, Path filePath, String filename, String contentType) {
        this.items.add(new FormDataItem(name, filePath, filename, contentType));
        return this;
    }

    /**
     * <p>remove.</p>
     *
     * @param name a {@link String} object
     * @return a {@link FormData} object
     */
    public FormData remove(String name) {
        this.items.removeIf(formDataItem -> name.equals(formDataItem.name()));
        return this;
    }

    public List<FormDataItem> items() {
        return items;
    }

    @Override
    public HttpRequest.BodyPublisher bodyPublisher(Builder builder) {
        var tempRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        var encoder = wrap(() -> initEncoder(this, tempRequest));
        //这里主要是为了将 tempRequest 中的 content-type 同步写入到 request 中
        tempRequest.headers().forEach((k) -> builder.header(k.getKey(), k.getValue()));
        var formDataIterable = new FormDataIterable(encoder);
        return BodyPublishers.ofByteArrays(formDataIterable);
    }

}
