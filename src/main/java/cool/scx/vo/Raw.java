package cool.scx.vo;

import cool.scx.enumeration.RawType;
import cool.scx.http.exception.impl.NotFoundException;
import cool.scx.util.exception.ScxExceptionHelper;
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
 * 原始文件 但不需要下载的 vo
 * 比如 pdf 之类
 *
 * @author scx567888
 * @version 0.7.0
 */
public final class Raw implements BaseVo {

    /**
     * 正则表达式 用于校验 RANGE 字段
     */
    private static final Pattern RANGE = Pattern.compile("^bytes=(\\d+)-(\\d*)$");

    /**
     * 读取流或者字节时的 分块大小
     */
    private static final int bucketSize = 1024 * 1000;

    /**
     * 字节数组
     */
    final byte[] bytes;

    /**
     * 数据来源
     */
    final Type type;

    /**
     * 输入流
     */
    private final InputStream inputStream;

    /**
     * 路径
     */
    private final Path path;

    /**
     * 文件类型 主要用于当数据类型是 byte 时, 确定像前台发送的 ContentType
     */
    private final RawType rawType;

    private Raw(InputStream inputStream, Path path, byte[] bytes, Type type, RawType rawType) {
        this.inputStream = inputStream;
        this.path = path;
        this.bytes = bytes;
        this.type = type;
        this.rawType = rawType;
    }

    public static Raw of(byte[] bytes, RawType rawType) {
        return new Raw(null, null, bytes, Type.BYTE_ARRAY, rawType);
    }

    public static Raw of(Path path) {
        return new Raw(null, path, null, Type.PATH, null);
    }

    public static Raw of(InputStream inputStream, RawType rawType) {
        return new Raw(inputStream, null, null, Type.INPUT_STREAM, rawType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext context) {
        switch (type) {
            case BYTE_ARRAY -> sendBytes(context);
            case PATH -> sendFile(context);
            case INPUT_STREAM -> sendInputStream(context);
        }
    }

    /**
     * <p>sendFile.</p>
     *
     * @param context c
     * @throws cool.scx.http.exception.impl.NotFoundException if any.
     */
    private void sendFile(RoutingContext context) throws NotFoundException {
        this.writeFile(context);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    private void sendBytes(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline")
                .putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(bytes.length));
        this.writeBytes(response, 0);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    void sendInputStream(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .setChunked(true)
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline");
        this.writeInputStream(response);
    }

    /**
     * <p>writeBytes.</p>
     *
     * @param response response
     */
    void writeInputStream(HttpServerResponse response) {
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
     * <p>writeBytes.</p>
     *
     * @param response   response
     * @param startIndex 起始索引
     */
    void writeBytes(HttpServerResponse response, int startIndex) {
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
     * <p>writeFile.</p>
     *
     * @param context a {@link HttpServerResponse} object
     * @throws NotFoundException if any.
     */
    void writeFile(RoutingContext context) throws NotFoundException {
        var file = path.toFile();
        if (file == null || !file.exists()) {
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
            VoHelper.fillContentType(MimeMapping.getMimeTypeForFilename(file.getName()), response);
            if (offset != null) {
                // must return content range
                headers.set(HttpHeaderNames.CONTENT_RANGE, "bytes " + offset + "-" + end + "/" + file.length());
                // return a partial response
                response.setStatusCode(HttpResponseStatus.PARTIAL_CONTENT.code());

                long finalOffset = offset;
                long finalLength = end + 1 - offset;

                response.sendFile(file.getPath(), finalOffset, finalLength, res2 -> {
                    if (res2.failed()) {
                        context.fail(res2.cause());
                    }
                });
            } else {

                response.sendFile(file.getPath(), res2 -> {
                    if (res2.failed()) {
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
