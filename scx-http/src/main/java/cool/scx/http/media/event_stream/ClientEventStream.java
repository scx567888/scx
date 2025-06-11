package cool.scx.http.media.event_stream;

import cool.scx.byte_reader.ByteReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static cool.scx.http.media.event_stream.EventStreamHelper.LF_BYTES;
import static cool.scx.io.IOHelper.inputStreamToByteReader;

// todo 整体待重构
// todo 这里和 MultiPartStream 一样 没有正确处理 inputStream 的关闭
public class ClientEventStream {

    private final ByteReader dataReader;
    private final Charset charset;

    public ClientEventStream(InputStream in, Charset charset) {
        this.dataReader = inputStreamToByteReader(in);
        this.charset = charset;
    }


    // 读取事件
    public SseEvent readEvent() throws IOException {
        var event = SseEvent.of();

        // 解析事件
        while (true) {

            var bytes = dataReader.readUntil(LF_BYTES);

            var line = new String(bytes, charset);

            if (line.isEmpty()) {
                // 事件结束，返回 SseEvent
                return event;
            }

            if (line.startsWith("event: ")) {
                event.event(line.substring(7).trim());
            } else if (line.startsWith("data: ")) {
                event.data(line.substring(6).trim());
            } else if (line.startsWith("id: ")) {
                event.id(line.substring(4).trim());
            } else if (line.startsWith("retry: ")) {
                try {
                    event.retry(Long.parseLong(line.substring(7).trim()));
                } catch (NumberFormatException e) {
                    // 如果格式错误，可以忽略
                }
            } else if (line.startsWith(": ")) {
                event.comment(line.substring(2).trim());
            }
        }
    }

}
