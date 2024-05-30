package cool.scx.common.http_client.request_body;

import cool.scx.common.http_client.ScxHttpClientRequestBody;
import cool.scx.common.http_client.request_body.form_data.FormDataHelper;
import cool.scx.common.http_client.request_body.form_data.FormDataItem;
import cool.scx.common.http_client.request_body.form_data.FormDataIterable;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.common.util.ScxExceptionHelper.wrap;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArrays;

/**
 * 使用 netty 的 HttpPostRequestEncoder 简化 FormData 的创建
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class FormData implements ScxHttpClientRequestBody {

    private final List<FormDataItem> items = new ArrayList<>();

    public FormData attribute(String name, Object text) {
        this.items.add(new FormDataItem(name, text.toString()));
        return this;
    }

    public FormData fileUpload(String name, byte[] fileByte) {
        this.items.add(new FormDataItem(name, fileByte));
        return this;
    }

    public FormData fileUpload(String name, byte[] fileByte, String filename) {
        this.items.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    public FormData fileUpload(String name, byte[] fileByte, String filename, String contentType) {
        this.items.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    public FormData fileUpload(String name, Path filePath) {
        this.items.add(new FormDataItem(name, filePath));
        return this;
    }

    public FormData fileUpload(String name, Path filePath, String filename) {
        this.items.add(new FormDataItem(name, filePath, filename));
        return this;
    }

    public FormData fileUpload(String name, Path filePath, String filename, String contentType) {
        this.items.add(new FormDataItem(name, filePath, filename, contentType));
        return this;
    }

    public FormData remove(String name) {
        this.items.removeIf(formDataItem -> name.equals(formDataItem.name()));
        return this;
    }

    public List<FormDataItem> items() {
        return items;
    }

    @Override
    public BodyPublisher bodyPublisher(Builder builder) {
        var tempRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        var encoder = wrap(() -> FormDataHelper.initEncoder(this, tempRequest));
        //这里主要是为了将 tempRequest 中的 content-type 同步写入到 request 中
        tempRequest.headers().forEach((k) -> builder.header(k.getKey(), k.getValue()));
        var formDataIterable = new FormDataIterable(encoder);
        return ofByteArrays(formDataIterable);
    }

}
