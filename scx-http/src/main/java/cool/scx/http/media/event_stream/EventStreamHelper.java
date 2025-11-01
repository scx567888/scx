package cool.scx.http.media.event_stream;

import cool.scx.io.ByteOutput;

import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EventStreamHelper {

    public static final byte[] LF_BYTES = "\n".getBytes();

    public static void writeToOutputStream(SseEvent sseEvent, ByteOutput out) throws IOException {
        // 获取事件的各个部分
        var event = sseEvent.event();
        var data = sseEvent.data();
        var id = sseEvent.id();
        var retry = sseEvent.retry();
        var comment = sseEvent.comment();

        // 使用 StringBuilder 拼接事件内容
        StringBuilder sb = new StringBuilder();

        // 可选评论（如果有的话）
        if (comment != null) {
            sb.append(": ").append(comment).append("\n");
        }

        // 事件类型（如果没有提供, 则使用 "message" 作为默认值）
        if (event != null) {
            sb.append("event: ").append(event).append("\n");
        } else {
            sb.append("event: message\n");  // 默认事件类型
        }

        // 事件数据
        if (data != null) {
            sb.append("data: ").append(data).append("\n");
        }

        // 事件 ID
        if (id != null) {
            sb.append("id: ").append(id).append("\n");
        }

        // 重试时间
        if (retry != null) {
            sb.append("retry: ").append(retry).append("\n");
        }

        // 事件结束, 追加空行（用于分隔事件）
        sb.append("\n");

        // 写入输出流
        out.write(sb.toString().getBytes(UTF_8)); // 使用 UTF-8 编码
        out.flush();  // 确保数据已发送
    }


}
