package cool.scx.http.media.event_stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.media.event_stream.EventStreamHelper.writeToOutputStream;

public class EventStream implements AutoCloseable{

    private final OutputStream out;

    public EventStream(OutputStream out) {
        this.out = out;
    }

    /// 发送一个 SSE 事件
    ///
    /// @param sseEvent 要发送的 SSE 事件
    public void send(SseEvent sseEvent) {
        try {
            writeToOutputStream(sseEvent, out);  // 使用 SseEvent 的 writeToOutputStream 方法发送事件
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();  // 关闭输出流
    }

}
