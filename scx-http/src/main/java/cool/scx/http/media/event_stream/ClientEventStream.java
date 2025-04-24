package cool.scx.http.media.event_stream;

import cool.scx.io.data_reader.LinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import static cool.scx.http.media.event_stream.EventStreamHelper.LF_BYTES;
import static cool.scx.io.IOHelper.inputStreamToDataReader;

// todo 整体待重构
// todo 这里参考 websocket 的设计 以流的形式来看待 然后 抽取出一个 EventClientEventStream 来承接事件模型
// todo 这里和 MultiPartStream 一样 没有正确处理 inputStream 的关闭
public class ClientEventStream {

    private final LinkedDataReader dataReader;
    private final Charset charset;
    private Consumer<SseEvent> eventHandler;
    private boolean running;

    public ClientEventStream(InputStream in, Charset charset) {
        this.dataReader = inputStreamToDataReader(in);
        this.charset = charset;
        this.running = true;
    }

    // 注册事件处理器
    public void onEvent(Consumer<SseEvent> eventHandler) {
        this.eventHandler = eventHandler;
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

    // 启动事件流读取
    public void start() {
        while (running) {
            try {
                SseEvent event = readEvent();
                callEventHandler(event);
            } catch (IOException e) {
                // 处理 IO 异常，可能是连接关闭
                break;
            }
        }
    }

    // 调用事件处理器
    public void callEventHandler(SseEvent sseEvent) {
        if (eventHandler != null) {
            eventHandler.accept(sseEvent);
        }
    }

    // 停止事件流
    public void stop() {
        this.running = false;
    }

}
