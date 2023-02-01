package cool.scx.mvc.vo;

import cool.scx.mvc.exception.NotFoundException;
import cool.scx.util.ScxExceptionHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本写入程序 可以直接向相应体中写入数据
 *
 * @author scx567888
 * @version 1.16.4
 */
class BaseWriter implements BaseVo {

    /**
     * 正则表达式 用于校验 RANGE 字段
     */
    private static final Pattern RANGE = Pattern.compile("^bytes=(\\d+)-(\\d*)$");

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
    protected final String contentType;

    /**
     * a
     */
    protected final String contentDisposition;

    /**
     * <p>Constructor for BaseWriter.</p>
     *
     * @param inputStream        a {@link java.io.InputStream} object
     * @param path               a {@link java.nio.file.Path} object
     * @param bytes              an array of {@link byte} objects
     * @param type               a {@link BaseWriter.Type} object
     * @param contentType        a {@link java.lang.String} object
     * @param contentDisposition a {@link java.lang.String} object
     */
    private BaseWriter(InputStream inputStream, Path path, byte[] bytes, Type type, String contentType, String contentDisposition) {
        this.inputStream = inputStream;
        this.path = path;
        this.bytes = bytes;
        this.type = type;
        this.contentType = contentType;
        this.contentDisposition = contentDisposition;
    }

    /**
     * <p>Constructor for BaseWriter.</p>
     *
     * @param inputStream        a {@link java.io.InputStream} object
     * @param contentType        a {@link java.lang.String} object
     * @param contentDisposition a {@link java.lang.String} object
     */
    protected BaseWriter(InputStream inputStream, String contentType, String contentDisposition) {
        this(inputStream, null, null, Type.INPUT_STREAM, contentType, contentDisposition);
    }

    /**
     * <p>Constructor for BaseWriter.</p>
     *
     * @param path               a {@link java.nio.file.Path} object
     * @param contentType        a {@link java.lang.String} object
     * @param contentDisposition a {@link java.lang.String} object
     */
    protected BaseWriter(Path path, String contentType, String contentDisposition) {
        this(null, path, null, Type.PATH, contentType, contentDisposition);
    }

    /**
     * <p>Constructor for BaseWriter.</p>
     *
     * @param bytes              an array of {@link byte} objects
     * @param contentType        a {@link java.lang.String} object
     * @param contentDisposition a {@link java.lang.String} object
     */
    protected BaseWriter(byte[] bytes, String contentType, String contentDisposition) {
        this(null, null, bytes, Type.BYTE_ARRAY, contentType, contentDisposition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void accept(RoutingContext context) {
        switch (type) {
            case BYTE_ARRAY -> sendBytes(context);
            case PATH -> sendFile(context);
            case INPUT_STREAM -> sendInputStream(context);
        }
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    protected void sendBytes(RoutingContext context) {
        BaseVo.fillContentType(contentType, context.request().response()).putHeader(HttpHeaderNames.CONTENT_DISPOSITION, contentDisposition);
        this.writeBytes(context.response().putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(this.bytes.length)), 0);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    protected void sendInputStream(RoutingContext context) {
        BaseVo.fillContentType(contentType, context.request().response()).putHeader(HttpHeaderNames.CONTENT_DISPOSITION, contentDisposition);
        this.writeInputStream(context.response().setChunked(true));
    }

    /**
     * <p>sendFile.</p>
     *
     * @param context c
     * @throws NotFoundException if any.
     */
    protected void sendFile(RoutingContext context) throws NotFoundException {
        context.request().response().putHeader(HttpHeaderNames.CONTENT_DISPOSITION, contentDisposition);
        this.writeFile(context);
    }

    /**
     * <p>writeBytes.</p>
     *
     * @param response   response
     * @param startIndex 起始索引
     */
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

    /**
     * <p>writeBytes.</p>
     *
     * @param response response
     */
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

    /**
     * <p>writeFile.</p>
     *
     * @param context a {@link io.vertx.core.http.HttpServerResponse} object
     * @throws NotFoundException if any.
     */
    private void writeFile(RoutingContext context) throws NotFoundException {
        var file = path.toFile();
        if (!file.exists()) {
            throw new NotFoundException();
        }
        var request = context.request();
        var response = context.response();

        Long offset = null;
        long end;
        MultiMap headers;

        if (response.closed()) {
            return;
        }

        // check if the client is making a range request
        String range = request.getHeader("Range");
        // end byte is length - 1
        end = file.length() - 1;

        if (range != null) {
            Matcher m = RANGE.matcher(range);
            if (m.matches()) {
                try {
                    String part = m.group(1);
                    // offset cannot be empty
                    offset = Long.parseLong(part);
                    // offset must fall inside the limits of the file
                    if (offset < 0 || offset >= file.length()) {
                        throw new IndexOutOfBoundsException();
                    }
                    // length can be empty
                    part = m.group(2);
                    if (part != null && part.length() > 0) {
                        // ranges are inclusive
                        end = Math.min(end, Long.parseLong(part));
                        // end offset must not be smaller than start offset
                        if (end < offset) {
                            throw new IndexOutOfBoundsException();
                        }
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    context.response().putHeader(HttpHeaderNames.CONTENT_RANGE, "bytes */" + file.length());
                    context.request().resume();
                    context.fail(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE.code());
                    return;
                }
            }
        }

        // notify client we support range requests
        headers = response.headers();
        headers.set(HttpHeaderNames.ACCEPT_RANGES, "bytes");
        // send the content length even for HEAD requests
        headers.set(HttpHeaderNames.CONTENT_LENGTH, Long.toString(end + 1 - (offset == null ? 0 : offset)));

        if (request.method() == HttpMethod.HEAD) {
            response.end();
        } else {
            BaseVo.fillContentType(MimeMapping.getMimeTypeForFilename(file.getName()), response);
            if (offset != null) {
                // must return content range
                headers.set(HttpHeaderNames.CONTENT_RANGE, "bytes " + offset + "-" + end + "/" + file.length());
                // return a partial response
                response.setStatusCode(HttpResponseStatus.PARTIAL_CONTENT.code());

                long finalOffset = offset;
                long finalLength = end + 1 - offset;

                response.sendFile(file.getPath(), finalOffset, finalLength, res2 -> {
                    if (res2.failed()) {
                        context.request().resume();
                        context.fail(res2.cause());
                    }
                });
            } else {

                response.sendFile(file.getPath(), res2 -> {
                    if (res2.failed()) {
                        context.request().resume();
                        context.fail(res2.cause());
                    }
                });
            }

        }
    }

    enum Type {
        PATH, INPUT_STREAM, BYTE_ARRAY,
    }

}
