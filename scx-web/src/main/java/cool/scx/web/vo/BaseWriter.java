package cool.scx.web.vo;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.exception.NotFoundException;
import cool.scx.http.routing.RoutingContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cool.scx.http.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.http.HttpFieldName.CONTENT_LENGTH;
import static cool.scx.http.MediaType.APPLICATION_OCTET_STREAM;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 基本写入程序 可以直接向相应体中写入数据
 *
 * @author scx567888
 * @version 1.16.4
 */
class BaseWriter implements BaseVo {

    /**
     * 读取流或者字节时的 分块大小
     */
    private static final int bucketSize = 1024 * 1000;

    /**
     * 输入流
     */
    protected final InputStream inputStream;

    /**
     * 路径
     */
    protected final Path path;

    /**
     * 字节数组
     */
    protected final byte[] bytes;

    /**
     * 数据来源
     */
    protected final Type type;

    /**
     * a
     */
    protected final MediaType contentType;

    /**
     * a
     */
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

    @Override
    public final void accept(RoutingContext context) {
        switch (type) {
            case BYTE_ARRAY -> sendBytes(context);
            case PATH -> sendFile(context);
            case INPUT_STREAM -> sendInputStream(context);
        }
    }

    protected void sendBytes(RoutingContext context) {
        var response = fillContentType(context.request().response(), contentType)
                .setHeader(CONTENT_DISPOSITION, contentDisposition)
                .setHeader(CONTENT_LENGTH, String.valueOf(this.bytes.length));
        this.writeBytes(response, 0);
    }

    protected void sendInputStream(RoutingContext context) {
        var response = fillContentType(context.request().response(), contentType)
                .setHeader(CONTENT_DISPOSITION, contentDisposition);
        //todo ?
//                setChunked(true);
        this.writeInputStream(response);
    }

    protected void sendFile(RoutingContext context) throws NotFoundException {
        context.request().response().setHeader(CONTENT_DISPOSITION, contentDisposition);
        this.writeFile(context);
    }

    private void writeBytes(ScxHttpServerResponse response, int startIndex) {
        response.send(this.bytes);
    }

    private void writeInputStream(ScxHttpServerResponse response) {
        try {
            inputStream.transferTo(response.outputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(RoutingContext context) throws NotFoundException {
        System.out.println();
//        context.response().setHeader(CONTENT_LENGTH, Files.len);
        context.response().send(this.path);
        //todo 此处需要处理 206 之类的请求
//        SEND_FILE_HELPER.sendStatic(context, context.vertx().fileSystem(), this.path.toString(), false);
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

    enum Type {
        PATH, INPUT_STREAM, BYTE_ARRAY,
    }

}
