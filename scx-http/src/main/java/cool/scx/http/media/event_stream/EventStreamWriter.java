package cool.scx.http.media.event_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.ScxMediaType;

import java.io.OutputStream;

import static cool.scx.http.media_type.MediaType.TEXT_EVENT_STREAM;
import static java.nio.charset.StandardCharsets.UTF_8;

public class EventStreamWriter implements MediaWriter {

    private EventStream eventStream;

    public EventStreamWriter() {

    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(ScxMediaType.of(TEXT_EVENT_STREAM).charset(UTF_8));
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        eventStream = new EventStream(outputStream);
    }

    public EventStream eventStream() {
        return eventStream;
    }

}
