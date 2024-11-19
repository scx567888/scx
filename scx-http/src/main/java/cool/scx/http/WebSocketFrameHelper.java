package cool.scx.http;

import cool.scx.io.DataReader;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public class WebSocketFrameHelper {

    public static WebSocketFrame readFrame(DataReader reader) {
        byte[] header = reader.read(2);

        var b1 = header[0];
        var b2 = header[1];

        var fin = (b1 & 0b1000_0000) != 0;
        var rsv1 = (b1 & 0b0100_0000) != 0;
        var rsv2 = (b1 & 0b0010_0000) != 0;
        var rsv3 = (b1 & 0b0001_0000) != 0;

        var opCode = WebSocketOpCode.of(b1 & 0b0000_1111);

        var masked = (b2 & 0b1000_0000) != 0;
        long payloadLength = b2 & 0b0111_1111;

        // 读取扩展长度
        if (payloadLength == 126) {
            byte[] extendedPayloadLength = reader.read(2);
            payloadLength = (extendedPayloadLength[0] & 0xFFL) << 8 |
                    extendedPayloadLength[1] & 0xFFL;
        } else if (payloadLength == 127) {
            byte[] extendedPayloadLength = reader.read(8);
            payloadLength = (extendedPayloadLength[0] & 0xFFL) << 56 |
                    (extendedPayloadLength[1] & 0xFFL) << 48 |
                    (extendedPayloadLength[2] & 0xFFL) << 40 |
                    (extendedPayloadLength[3] & 0xFFL) << 32 |
                    (extendedPayloadLength[4] & 0xFFL) << 24 |
                    (extendedPayloadLength[5] & 0xFFL) << 16 |
                    (extendedPayloadLength[6] & 0xFFL) << 8 |
                    extendedPayloadLength[7] & 0xFFL;
        }

        byte[] maskingKey = null;

        if (masked) {
            maskingKey = reader.read(4);
        }

        // 我们假定长度都是在 int 范围内的 (理论上不会有 2GB 的文件会通过 websocket 发送)
        var payloadData = reader.read((int) payloadLength);

        // 掩码计算
        if (masked) {
            for (int i = 0; i < payloadLength; i++) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }

        return new WebSocketFrame(fin, rsv1, rsv2, rsv3, opCode, masked, payloadLength, maskingKey, payloadData);
    }

}
