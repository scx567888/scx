package cool.scx.http.web_socket;

import cool.scx.io.DataReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public class WebSocketFrameHelper {

    public static WebSocketFrame readFrame(DataReader reader) {
        byte[] header = reader.read(2);

        var b1 = header[0];

        var fin = (b1 & 0b1000_0000) != 0;
        var rsv1 = (b1 & 0b0100_0000) != 0;
        var rsv2 = (b1 & 0b0010_0000) != 0;
        var rsv3 = (b1 & 0b0001_0000) != 0;
        var opCode = WebSocketOpCode.of(b1 & 0b0000_1111);

        var b2 = header[1];

        var masked = (b2 & 0b1000_0000) != 0;
        int payloadLength = b2 & 0b0111_1111;

        // 读取扩展长度
        if (payloadLength == 126) {
            byte[] extendedPayloadLength = reader.read(2);
            payloadLength = (extendedPayloadLength[0] & 0b1111_1111) << 8 |
                    extendedPayloadLength[1] & 0b1111_1111;
        } else if (payloadLength == 127) {
            byte[] extendedPayloadLength = reader.read(8);
            // 我们假定长度都是在 int 范围内的 (理论上不会有 2GB 的文件会通过 websocket 发送)
            payloadLength = (int) ((extendedPayloadLength[0] & 0b1111_1111L) << 56 |
                    (extendedPayloadLength[1] & 0b1111_1111L) << 48 |
                    (extendedPayloadLength[2] & 0b1111_1111L) << 40 |
                    (extendedPayloadLength[3] & 0b1111_1111L) << 32 |
                    (extendedPayloadLength[4] & 0b1111_1111) << 24 |
                    (extendedPayloadLength[5] & 0b1111_1111) << 16 |
                    (extendedPayloadLength[6] & 0b1111_1111) << 8 |
                    extendedPayloadLength[7] & 0b1111_1111);
        }

        byte[] maskingKey = null;

        if (masked) {
            maskingKey = reader.read(4);
        }

        var payloadData = reader.read(payloadLength);

        // 掩码计算
        if (masked) {
            for (int i = 0; i < payloadLength; i = i + 1) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }

        return new WebSocketFrame(fin, rsv1, rsv2, rsv3, opCode, masked, payloadLength, maskingKey, payloadData);
    }

    public static void writeFrame(WebSocketFrame frame, OutputStream out) throws IOException {
        // 头部
        int fullOpCode = (frame.fin() ? 0b1000_0000 : 0) |
                (frame.rsv1() ? 0b0100_0000 : 0) |
                (frame.rsv2() ? 0b0010_0000 : 0) |
                (frame.rsv3() ? 0b0001_0000 : 0) |
                frame.opCode().code();

        //写入头部
        out.write(fullOpCode);

        var length = frame.payloadLength();
        var masked = frame.masked() ? 0b1000_0000 : 0;

        if (length < 126L) {
            out.write(length | masked);
        } else if (length < 65536L) {
            out.write(126 | masked);
            out.write((length >>> 8) & 0b1111_1111);
            out.write(length & 0b1111_1111);
        } else {
            out.write(127 | masked);
            for (int i = 56; i >= 0; i -= 8) {
                out.write((length >>> i) & 0b1111_1111);
            }
        }

        // 写入掩码键（如果有）
        if (frame.masked()) {
            byte[] maskingKey = frame.maskingKey();
            out.write(maskingKey);
        }

        // 处理掩码（如果有）
        byte[] payloadData = frame.payloadData();
        byte[] maskingKey = frame.maskingKey();
        if (frame.masked()) {
            for (int i = 0; i < payloadData.length; i = i + 1) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }

        // 写入有效负载数据
        out.write(payloadData);
        out.flush();
    }

    public static WebSocketFrame readFrameUntilLast(DataReader reader) {
        var frameList = new ArrayList<WebSocketFrame>();

        while (true) {
            var webSocketFrame = readFrame(reader);
            frameList.add(webSocketFrame);
            if (webSocketFrame.fin()) {
                break;
            }
        }

        var first = frameList.get(0);

        if (frameList.size() == 1) {
            return first;
        }

        var opCode = first.opCode();
        var length = frameList.stream().mapToInt(WebSocketFrame::payloadLength).sum();
        var payloadData = new byte[length];

        int offset = 0;
        for (var webSocketFrame : frameList) {
            System.arraycopy(webSocketFrame.payloadData(), 0, payloadData, offset, webSocketFrame.payloadLength());
            offset += webSocketFrame.payloadLength();
        }

        return WebSocketFrame.of(true, opCode, payloadData);

    }

}
