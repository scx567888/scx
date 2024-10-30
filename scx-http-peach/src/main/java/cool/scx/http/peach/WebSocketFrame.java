package cool.scx.http.peach;

import cool.scx.http.WebSocketOpCode;
import cool.scx.io.DataReader;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public class WebSocketFrame {

    private boolean fin;
    private WebSocketOpCode opCode;
    private boolean masked;
    private int payloadLength;
    private byte[] maskingKey;
    private byte[] payloadData;

    public WebSocketFrame(DataReader reader) {
        parseFrame(reader);
    }

    public void parseFrame(DataReader reader) {
        byte[] header = reader.read(2);

        var b1 = header[0];
        var b2 = header[1];

        fin = (b1 & 0b1000_0000) != 0;
        opCode = WebSocketOpCode.of(b1 & 0b0000_1111);

        masked = (b2 & 0b1000_0000) != 0;
        payloadLength = b2 & 0b0111_1111;

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

        if (masked) {
            maskingKey = reader.read(4);
        }

        payloadData = reader.read(payloadLength);

        //掩码计算
        if (masked) {
            for (int i = 0; i < payloadLength; i++) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }
    }

    public boolean fin() {
        return fin;
    }

    public WebSocketOpCode opCode() {
        return opCode;
    }

    public boolean masked() {
        return masked;
    }

    public int payloadLength() {
        return payloadLength;
    }

    public byte[] maskingKey() {
        return maskingKey;
    }

    public byte[] payloadData() {
        return payloadData;
    }

}
