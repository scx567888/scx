package cool.scx.http.media.event_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.ScxMediaType;

import java.io.OutputStream;

import static cool.scx.http.media_type.MediaType.TEXT_EVENT_STREAM;
import static java.nio.charset.StandardCharsets.UTF_8;

/// todo 整体待重构 比如没有正确处理 输出流的关闭
public class ServerEventStreamWriter implements MediaWriter {

    private ServerEventStream eventStream;

    public ServerEventStreamWriter() {

    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(ScxMediaType.of(TEXT_EVENT_STREAM).charset(UTF_8));
        }
        return -1;// 我们无法确定长度 因为 ServerEventStream 是由用户动态写入的
    }

    @Override
    public void write(OutputStream outputStream) {
        eventStream = new ServerEventStream(outputStream);
    }

    public ServerEventStream eventStream() {
        return eventStream;
    }

}
