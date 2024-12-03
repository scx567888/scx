package cool.scx.web.vo;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.routing.handler.StaticHelper;

import java.io.InputStream;
import java.nio.file.Path;

import static cool.scx.http.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.http.MediaType.APPLICATION_OCTET_STREAM;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 基本写入程序 可以直接向相应体中写入数据
 *
 * @author scx567888
 * @version 0.0.1
 */
class BaseWriter implements BaseVo {

    protected final InputStream inputStream;
    protected final Path path;
    protected final byte[] bytes;
    protected final Type type;
    protected final MediaType contentType;
    protected final String contentDisposition;

    private BaseWriter(InputStream inputStream, Path path, byte[] bytes, Type type, MediaType contentType, String contentDisposition) {
        this.inputStream = inputStream;
        this.path = path;
        this.bytes = bytes;
        this.type = type;
        this.contentType = contentType;
        this.contentDisposition = contentDisposition;
    }

    protected BaseWriter(InputStream inputStream, MediaType contentType, String contentDisposition) {
        this(inputStream, null, null, Type.INPUT_STREAM, contentType, contentDisposition);
    }

    protected BaseWriter(Path path, MediaType contentType, String contentDisposition) {
        this(null, path, null, Type.PATH, contentType, contentDisposition);
    }

    protected BaseWriter(byte[] bytes, MediaType contentType, String contentDisposition) {
        this(null, null, bytes, Type.BYTE_ARRAY, contentType, contentDisposition);
    }

    public static ScxHttpServerResponse fillContentType(ScxHttpServerResponse response, MediaType contentType) {
        if (contentType != null) {
            if (contentType.type().equals("text")) {
                return response.contentType(ContentType.of(contentType).charset(UTF_8));
            } else {
                return response.contentType(ContentType.of(contentType));
            }
        } else {
            return response.contentType(ContentType.of(APPLICATION_OCTET_STREAM));
        }
    }

    @Override
    public final void accept(RoutingContext context) {
        var response = context.response();
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);
        fillContentType(response, contentType);
        switch (type) {
            case BYTE_ARRAY -> response.send(this.bytes);
            case PATH -> StaticHelper.sendStatic(this.path, context);
            case INPUT_STREAM -> response.send(inputStream);
        }
    }

    enum Type {
        PATH, INPUT_STREAM, BYTE_ARRAY,
    }

}
