package cool.scx.web.vo;

import cool.scx.common.standard.MediaType;
import cool.scx.common.util.ScxExceptionHelper;
import cool.scx.web.exception.NotFoundException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;

import static cool.scx.common.standard.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.common.standard.HttpFieldName.CONTENT_LENGTH;
import static cool.scx.web.ScxWebHelper.fillContentType;
import static cool.scx.web._hack.SendFileHelper.SEND_FILE_HELPER;

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
                .putHeader(CONTENT_DISPOSITION.toString(), contentDisposition)
                .putHeader(CONTENT_LENGTH.toString(), String.valueOf(this.bytes.length));
        this.writeBytes(response, 0);
    }

    protected void sendInputStream(RoutingContext context) {
        var response = fillContentType(context.request().response(), contentType)
                .putHeader(CONTENT_DISPOSITION.toString(), contentDisposition).
                setChunked(true);
        this.writeInputStream(response);
    }

    protected void sendFile(RoutingContext context) throws NotFoundException {
        context.request().response().putHeader(CONTENT_DISPOSITION.toString(), contentDisposition);
        this.writeFile(context);
    }

    private void writeBytes(HttpServerResponse response, int startIndex) {
        //当前分块的尾部索引
        var endIndex = startIndex + bucketSize;
        //尾部索引 大于等于 字节长度 说明是最后一个区块
        if (endIndex >= this.bytes.length) {
            response.end(Buffer.buffer(Arrays.copyOfRange(this.bytes, startIndex, this.bytes.length)));
        } else {//不是最后一个区块
            response.write(Buffer.buffer(Arrays.copyOfRange(this.bytes, startIndex, endIndex)), (r) -> {
                if (r.succeeded()) {
                    //将尾部索引作为下一次递归的起始索引
                    writeBytes(response, endIndex);
                }
            });
        }
    }

    private void writeInputStream(HttpServerResponse response) {
        var b = new byte[bucketSize];
        var endIndex = ScxExceptionHelper.wrap(() -> inputStream.read(b));
        //已经读取完毕
        if (endIndex == -1) {
            response.end();
        } else {//还有数据
            response.write(Buffer.buffer(Arrays.copyOfRange(b, 0, endIndex)), (r) -> {
                if (r.succeeded()) {
                    writeInputStream(response);
                }
            });
        }
    }

    private void writeFile(RoutingContext context) throws NotFoundException {
        SEND_FILE_HELPER.sendStatic(context, context.vertx().fileSystem(), this.path.toString(), false);
    }

    enum Type {
        PATH, INPUT_STREAM, BYTE_ARRAY,
    }

}
