package cool.scx.http.handler;

import cool.scx.type.UploadedEntity;
import cool.scx.util.file.FileUtils;
import io.netty.handler.codec.DecoderException;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.impl.HttpUtils;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RoutingContextInternal;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>ScxBodyHandler class.</p>
 *
 * @author scx567888
 * @version 1.3.8
 */
public final class ScxBodyHandler implements Handler<RoutingContext> {

    /**
     * 默认允许的最大请求体大小
     */
    private long bodyLimit = FileUtils.displaySizeToLong("16384KB");

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
        var request = context.request();
        if (request.headers().contains(HttpHeaders.UPGRADE, HttpHeaders.WEBSOCKET, true)) {
            context.next();
            return;
        }
        if (!((RoutingContextInternal) context).seenHandler(RoutingContextInternal.BODY_HANDLER)) {
            BHandler handler = new BHandler(context);
            request.handler(handler);
            request.endHandler(v -> handler.end());
            ((RoutingContextInternal) context).visitHandler(RoutingContextInternal.BODY_HANDLER);
        } else {
            context.next();
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
                context.setBody(body);
                body = null;
                context.next();
            }
        }

    }

}
