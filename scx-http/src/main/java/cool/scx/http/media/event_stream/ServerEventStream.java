package cool.scx.http.media.event_stream;

import cool.scx.http.sender.HttpSendException;
import cool.scx.io.ByteOutput;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media.event_stream.EventStreamHelper.writeToOutputStream;

public class ServerEventStream implements AutoCloseable {

    private final ByteOutput out;

    public ServerEventStream(ByteOutput out) {
        this.out = out;
    }

    /// 发送一个 SSE 事件
    ///
    /// @param sseEvent 要发送的 SSE 事件
    public void send(SseEvent sseEvent) throws HttpSendException {
        try {
            writeToOutputStream(sseEvent, out);  // 使用 SseEvent 的 writeToOutputStream 方法发送事件
        } catch (IOException e) {
            throw new HttpSendException("发送 EventStream 时发生异常 !!!", e);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();  // 关闭输出流
    }

}
