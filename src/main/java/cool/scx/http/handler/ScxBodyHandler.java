package cool.scx.http.handler;

import cool.scx.ScxConstant;
import cool.scx.type.UploadedEntity;
import io.netty.handler.codec.DecoderException;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.impl.HttpUtils;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RoutingContextInternal;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>ScxBodyHandler class.</p>
 * 使用内存暂存上传文件
 *
 * @author scx567888
 * @version 1.3.8
 */
public final class ScxBodyHandler implements Handler<RoutingContext> {

    /**
     * 默认允许的最大请求体大小
     */
    private long bodyLimit = ScxConstant.DEFAULT_BODY_LIMIT;

    /**
     * a
     *
     * @return a
     */
    public long bodyLimit() {
        return bodyLimit;
    }

    /**
     * a
     *
     * @param bodyLimit a
     */
    public void bodyLimit(long bodyLimit) {
        this.bodyLimit = bodyLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext context) {
        final HttpServerRequest request = context.request();
        final HttpServerResponse response = context.response();

        // we need to keep state since we can be called again on reroute
        if (!((RoutingContextInternal) context).seenHandler(RoutingContextInternal.BODY_HANDLER)) {
            ((RoutingContextInternal) context).visitHandler(RoutingContextInternal.BODY_HANDLER);

            // Check if a request has a request body.
            // A request with a body __must__ either have `transfer-encoding`
            // or `content-length` headers set.
            // http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3
            final long parsedContentLength = parseContentLengthHeader(request);

            if (!request.headers().contains(HttpHeaders.TRANSFER_ENCODING) && parsedContentLength == -1) {
                // there is no "body", so we can skip this handler
                context.next();
                return;
            }

            // before parsing the body we can already discard a bad request just by inspecting the content-length against
            // the body limit, this will reduce load, on the server by totally skipping parsing the request body
            if (bodyLimit != -1 && parsedContentLength != -1) {
                if (parsedContentLength > bodyLimit) {
                    context.fail(413);
                    return;
                }
            }

            // handle expectations
            // https://httpwg.org/specs/rfc7231.html#header.expect
            final String expect = request.getHeader(HttpHeaders.EXPECT);
            if (expect != null) {
                // requirements validation
                if (expect.equalsIgnoreCase("100-continue")) {
                    // A server that receives a 100-continue expectation in an HTTP/1.0 request MUST ignore that expectation.
                    if (request.version() != HttpVersion.HTTP_1_0) {
                        // signal the client to continue
                        response.writeContinue();
                    }
                } else {
                    // the server cannot meet the expectation, we only know about 100-continue
                    context.fail(417);
                    return;
                }
            }

            final BHandler handler = new BHandler(context);
            request
                    // resume the request (if paused)
                    .resume()
                    .handler(handler)
                    .endHandler(v -> handler.end());
        } else {
            context.next();
        }
    }

    /**
     * <p>parseContentLengthHeader.</p>
     *
     * @param request a {@link io.vertx.core.http.HttpServerRequest} object
     * @return a long
     */
    private long parseContentLengthHeader(HttpServerRequest request) {
        String contentLength = request.getHeader(HttpHeaders.CONTENT_LENGTH);
        if (contentLength == null || contentLength.isEmpty()) {
            return -1;
        }
        try {
            long parsedContentLength = Long.parseLong(contentLength);
            return parsedContentLength < 0 ? -1 : parsedContentLength;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private class BHandler implements Handler<Buffer> {

        final RoutingContext context;
        final boolean noNeedAppendBody; //为 true 表示不需要将 Buffer 追加到 body 中
        final AtomicInteger uploadCount = new AtomicInteger();
        Buffer body = Buffer.buffer(1024);
        boolean failed;
        boolean ended;
        long uploadSize = 0L;

        public BHandler(RoutingContext context) {
            this.context = context;
            var uploadedEntityMap = new HashMap<String, UploadedEntity>();
            context.put("uploadedEntityMap", uploadedEntityMap);

            final String contentType = context.request().getHeader(HttpHeaders.CONTENT_TYPE);

            // 判断是否不需要将 Buffer 追加到 body 中
            // 因为满足条件时 调用 setExpectMultipart(true) , 其内部的 decoder 便会对请求体进行解码
            // 并直接存储到 formAttributes 中 后续处理 直接从 formAttributes 读取即可
            noNeedAppendBody = contentType != null && HttpUtils.isValidMultipartContentType(contentType);

            if (noNeedAppendBody) {
                context.request().setExpectMultipart(true);

                context.request().uploadHandler(upload -> {
                    if (upload.isSizeAvailable()) {
                        long size = uploadSize + upload.size();
                        if (size > bodyLimit) {
                            failed = true;
                            context.fail(413);
                            return;
                        }
                    }

                    uploadCount.incrementAndGet();

                    var tempBuffer = Buffer.buffer();
                    upload.handler(tempBuffer::appendBuffer)
                            .endHandler(v -> {
                                uploadedEntityMap.put(upload.name(), new UploadedEntity(upload.name(), upload.filename(), upload.size(), tempBuffer, upload.contentType(), upload.charset()));
                                uploadEnded();
                            })
                            .exceptionHandler(ar -> context.fail(ar.getCause()));
                });
            }

            context.request().exceptionHandler(t -> {
                if (t instanceof DecoderException) {
                    context.fail(400, t.getCause());
                } else {
                    context.fail(t);
                }
            });
        }

        @Override
        public void handle(Buffer buff) {
            if (!failed) {
                uploadSize += buff.length();
                if (uploadSize > bodyLimit) {
                    failed = true;
                    context.fail(413);
                } else if (!noNeedAppendBody) {
                    body.appendBuffer(buff);
                }
            }
        }

        private void uploadEnded() {
            int count = uploadCount.decrementAndGet();
            if (ended && count == 0) {
                doEnd();
            }
        }

        private void end() {
            ended = true;
            if (uploadCount.get() == 0) {
                doEnd();
            }
        }

        private void doEnd() {
            if (!failed) {
                ((RoutingContextInternal) context).setBody(body);
                body = null;
                context.next();
            }
        }

    }

}
