package cool.scx.web.vo;

import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.media_type.MediaType;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.routing.handler.StaticHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static cool.scx.http.headers.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.http.media_type.MediaType.APPLICATION_OCTET_STREAM;
import static java.nio.charset.StandardCharsets.UTF_8;

/// 基本写入程序 可以直接向相应体中写入数据
///
/// @author scx567888
/// @version 0.0.1
class BaseWriter implements BaseVo {

    protected final InputStream inputStream;
    protected final File file;
    protected final byte[] bytes;
    protected final Type type;
    protected final MediaType contentType;
    protected final String contentDisposition;

    private BaseWriter(InputStream inputStream, File file, byte[] bytes, Type type, MediaType contentType, String contentDisposition) {
        this.inputStream = inputStream;
        this.file = file;
        this.bytes = bytes;
        this.type = type;
        this.contentType = contentType;
        this.contentDisposition = contentDisposition;
    }

    protected BaseWriter(InputStream inputStream, MediaType contentType, String contentDisposition) {
        this(inputStream, null, null, Type.INPUT_STREAM, contentType, contentDisposition);
    }

    protected BaseWriter(File file, MediaType contentType, String contentDisposition) {
        this(null, file, null, Type.PATH, contentType, contentDisposition);
    }

    protected BaseWriter(byte[] bytes, MediaType contentType, String contentDisposition) {
        this(null, null, bytes, Type.BYTE_ARRAY, contentType, contentDisposition);
    }

    public static ScxHttpServerResponse fillContentType(ScxHttpServerResponse response, MediaType contentType) {
        if (contentType == null) {
            return response.contentType(APPLICATION_OCTET_STREAM);
        }
        //文本我们统一加上 UTF-8 编码
        if (contentType.type().equals("text") && contentType.charset() == null) {
            return response.contentType(ScxMediaType.of(contentType).charset(UTF_8));
        } else {
            return response.contentType(contentType);
        }
    }

    @Override
    public final void apply(RoutingContext context) throws IOException {
        var response = context.response();
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);
        fillContentType(response, contentType);
        switch (type) {
            case BYTE_ARRAY -> response.send(this.bytes);
            case PATH -> StaticHelper.sendStatic(this.file, context);
            case INPUT_STREAM -> response.send(inputStream);
        }
    }

    enum Type {
        PATH, INPUT_STREAM, BYTE_ARRAY,
    }

}
