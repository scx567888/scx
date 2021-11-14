package cool.scx.util;

import cool.scx.exception.NotFoundException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>VoFileWriter class.</p>
 *
 * @author scx567888
 * @version 1.3.5
 */
public record VoFileWriter(File file) {

    /**
     * 正则表达式 用于校验 RANGE 字段
     */
    private static final Pattern RANGE = Pattern.compile("^bytes=(\\d+)-(\\d*)$");

    /**
     * <p>writeFile.</p>
     *
     * @param context a {@link HttpServerResponse} object
     * @throws NotFoundException if any.
     */
    public void writeFile(RoutingContext context) throws NotFoundException {
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

}
