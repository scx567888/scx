package cool.scx.util;

import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.ScxExceptionHelper.wrap;
import static cool.scx.util.StringUtils.notBlank;
import static io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.EncoderMode;
import static io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;
import static io.vertx.core.http.impl.MimeMapping.getMimeTypeForExtension;
import static io.vertx.core.http.impl.MimeMapping.getMimeTypeForFilename;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * 使用 netty 的 HttpPostRequestEncoder 简化 FormData 的创建
 * <p>
 * 可以直接调用 write() 进行写入 (异步)
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class FormData {

    /**
     * Constant <code>ALLOC</code>
     */
    private static final UnpooledByteBufAllocator ALLOC = new UnpooledByteBufAllocator(false);

    private final List<FormDataItem> items = new ArrayList<>();

    /**
     * 创建编码器
     *
     * @param formData FormData
     * @param request  a {@link io.netty.handler.codec.http.HttpRequest} object
     * @return a {@link io.netty.handler.codec.http.multipart.HttpPostRequestEncoder} object
     * @throws io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException if any.
     * @throws java.io.IOException                                                                    if any.
     */
    private static HttpPostRequestEncoder initEncoder(FormData formData, HttpRequest request) throws ErrorDataEncoderException, IOException {
        var encoder = new HttpPostRequestEncoder(new DefaultHttpDataFactory(), request, true, UTF_8, EncoderMode.HTML5);
        for (var formDataPart : formData.items) {
            switch (formDataPart.type) {
                case ATTRIBUTE -> encoder.addBodyAttribute(
                        formDataPart.name,
                        formDataPart.attributeValue
                );
                case FILE_UPLOAD_PATH -> encoder.addBodyFileUpload(
                        formDataPart.name,
                        formDataPart.filename,
                        formDataPart.fileUploadPath.toFile(),
                        formDataPart.contentType,
                        false
                );
                case FILE_UPLOAD_BYTES -> {
                    var fileUpload = new MemoryFileUpload(
                            formDataPart.name,
                            formDataPart.filename,
                            formDataPart.contentType,
                            null,
                            null,
                            formDataPart.fileUploadPathBytes.length
                    );
                    fileUpload.setContent(Unpooled.buffer().writeBytes(formDataPart.fileUploadPathBytes));
                    encoder.addBodyHttpData(fileUpload);
                }
            }
        }
        encoder.finalizeRequest();
        return encoder;
    }

    /**
     * <p>write0.</p>
     *
     * @param clientRequest a {@link io.vertx.core.http.HttpClientRequest} object
     * @param encoder       a {@link io.netty.handler.codec.http.multipart.HttpPostRequestEncoder} object
     * @throws java.lang.Exception if any.
     */
    private static void write0(HttpClientRequest clientRequest, HttpPostRequestEncoder encoder) throws Exception {
        if (!encoder.isEndOfInput()) {
            var chunk = encoder.readChunk(ALLOC);
            var content = chunk.content();
            var buff = Buffer.buffer(content);
            //todo 因为没有 await 所以只能用递归的方式写入 (可能造成栈溢出)
            clientRequest.write(buff).onSuccess(c -> wrap(() -> write0(clientRequest, encoder)));
        } else {
            clientRequest.end();
        }
    }

    /**
     * <p>attribute.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param text a {@link java.lang.Object} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData attribute(String name, Object text) {
        this.items.add(new FormDataItem(name, text.toString()));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param fileByte an array of {@link byte} objects
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte) {
        this.items.add(new FormDataItem(name, fileByte));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param fileByte an array of {@link byte} objects
     * @param filename a {@link java.lang.String} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte, String filename) {
        this.items.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name        a {@link java.lang.String} object
     * @param fileByte    an array of {@link byte} objects
     * @param filename    a {@link java.lang.String} object
     * @param contentType a {@link java.lang.String} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, byte[] fileByte, String filename, String contentType) {
        this.items.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param filePath a {@link java.nio.file.Path} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, Path filePath) {
        this.items.add(new FormDataItem(name, filePath));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param filePath a {@link java.nio.file.Path} object
     * @param filename a {@link java.lang.String} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, Path filePath, String filename) {
        this.items.add(new FormDataItem(name, filePath, filename));
        return this;
    }

    /**
     * <p>fileUpload.</p>
     *
     * @param name        a {@link java.lang.String} object
     * @param filePath    a {@link java.nio.file.Path} object
     * @param filename    a {@link java.lang.String} object
     * @param contentType a {@link java.lang.String} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData fileUpload(String name, Path filePath, String filename, String contentType) {
        this.items.add(new FormDataItem(name, filePath, filename, contentType));
        return this;
    }

    /**
     * <p>remove.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link cool.scx.util.FormData} object
     */
    public FormData remove(String name) {
        this.items.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    /**
     * <p>write.</p>
     *
     * @param request a {@link io.vertx.core.http.HttpClientRequest} object
     */
    public void write(HttpClientRequest request) {
        wrap(() -> {
            request.setChunked(true);
            var tempRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
            var encoder = initEncoder(this, tempRequest);
            //这里主要是为了将 tempRequest 中的 content-type 同步写入到 request 中
            tempRequest.headers().forEach((k) -> request.putHeader(k.getKey(), k.getValue()));
            write0(request, encoder);
        });
    }

    enum FormDataItemType {
        ATTRIBUTE,
        FILE_UPLOAD_PATH,
        FILE_UPLOAD_BYTES,
    }

    static final class FormDataItem {
        private final FormDataItemType type;
        private final String name;
        private final String filename;
        private final String contentType;
        private String attributeValue;
        private Path fileUploadPath;
        private byte[] fileUploadPathBytes;

        FormDataItem(String name, String value) {
            this.type = FormDataItemType.ATTRIBUTE;
            this.name = requireNonNull(name);
            this.attributeValue = value;
            this.filename = null;
            this.contentType = null;
        }

        FormDataItem(String name, byte[] content) {
            this(name, content, RandomUtils.randomUUID());
        }

        FormDataItem(String name, byte[] content, String filename) {
            this(name, content, filename, getMimeTypeForFilename(filename));
        }

        FormDataItem(String name, byte[] content, String filename, String contentType) {
            this.type = FormDataItemType.FILE_UPLOAD_BYTES;
            this.name = requireNonNull(name);
            this.fileUploadPathBytes = content;
            this.filename = notBlank(filename) ? filename : RandomUtils.randomUUID();
            this.contentType = notBlank(contentType) ? contentType : getMimeTypeForExtension("bin");
        }

        FormDataItem(String name, Path filePath) {
            this(name, filePath, filePath.getFileName().toString());
        }

        FormDataItem(String name, Path filePath, String filename) {
            this(name, filePath, filename, getMimeTypeForFilename(filename));
        }

        FormDataItem(String name, Path filePath, String filename, String contentType) {
            this.type = FormDataItemType.FILE_UPLOAD_PATH;
            this.name = requireNonNull(name);
            this.fileUploadPath = filePath;
            this.filename = notBlank(filename) ? filename : RandomUtils.randomUUID();
            this.contentType = notBlank(contentType) ? contentType : getMimeTypeForExtension("bin");
        }

    }

}
