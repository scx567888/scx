package cool.scx.http.peach;

import cool.scx.http.WebSocketOpCode;
import cool.scx.io.DataReader;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public class WebSocketFrameHelper {

    public static WebSocketFrame readFrame(DataReader reader) {
        byte[] header = reader.read(2);

        var b1 = header[0];
        var b2 = header[1];

        var fin = (b1 & 0b1000_0000) != 0;
        var opCode = WebSocketOpCode.of(b1 & 0b0000_1111);

        var masked = (b2 & 0b1000_0000) != 0;
        var payloadLength = b2 & 0b0111_1111;

        //读取 拓展长度
        if (payloadLength == 126) {
            var extendedPayloadLength = reader.read(2);
            payloadLength = (extendedPayloadLength[0] << 8) | (extendedPayloadLength[1] & 0xFF);
        } else if (payloadLength == 127) {
            var extendedPayloadLength = reader.read(8);
            payloadLength = 0;
            for (int i = 0; i < 8; i++) {
                payloadLength = (payloadLength << 8) | (extendedPayloadLength[i] & 0xFF);
            }
        }

        byte[] maskingKey = null;

        if (masked) {
            maskingKey = reader.read(4);
        }

        var payloadData = reader.read(payloadLength);

        //掩码计算
        if (masked) {
            for (int i = 0; i < payloadLength; i++) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }

        return new WebSocketFrame(fin, opCode, masked, payloadLength, maskingKey, payloadData);
    }

    private void writeFrame(WebSocketFrame frame, OutputStream out) throws IOException, IOException {
        // 写入头部
        int b1 = (frame.fin() ? 0x80 : 0) | frame.opCode().code();
        int b2 = frame.payloadLength();
        if (frame.masked()) {
            b2 |= 0x80;
        }
        out.write(b1);
        out.write(b2);

        // 写入扩展长度
        if (frame.payloadLength() >= 126) {
            if (frame.payloadLength() <= 0xFFFF) {
                out.write((frame.payloadLength() >> 8) & 0xFF);
                out.write(frame.payloadLength() & 0xFF);
            } else {
                for (int i = 7; i >= 0; i--) {
                    out.write((frame.payloadLength() >> (8 * i)) & 0xFF);
                }
            }
        }

        // 写入掩码键
        if (frame.masked() && frame.maskingKey() != null) {
            out.write(frame.maskingKey());
        }

        // 写入有效载荷数据
        byte[] payloadData = frame.payloadData();
        if (frame.masked() && frame.maskingKey() != null) {
            for (int i = 0; i < payloadData.length; i++) {
                payloadData[i] = (byte) (payloadData[i] ^ frame.maskingKey()[i % 4]);
            }
        }
        out.write(payloadData);
        out.flush();
    }

}
