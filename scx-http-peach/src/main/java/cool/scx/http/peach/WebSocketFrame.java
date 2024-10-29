package cool.scx.http.peach;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class WebSocketFrame {
    private boolean fin;
    private int opcode;
    private boolean masked;
    private long payloadLength;
    private byte[] maskingKey;
    private byte[] payloadData;

    public WebSocketFrame(InputStream inputStream) throws Exception {
        parseFrame(inputStream);
    }

    private void parseFrame(InputStream inputStream) throws Exception {
        byte[] header = new byte[2];
        inputStream.read(header, 0, 2);

        fin = (header[0] & 0x80) != 0;
        opcode = header[0] & 0x0F;
        masked = (header[1] & 0x80) != 0;
        payloadLength = header[1] & 0x7F;

        if (payloadLength == 126) {
            byte[] extendedPayloadLength = new byte[2];
            inputStream.read(extendedPayloadLength);
            payloadLength = ByteBuffer.wrap(extendedPayloadLength).getShort();
        } else if (payloadLength == 127) {
            byte[] extendedPayloadLength = new byte[8];
            inputStream.read(extendedPayloadLength);
            payloadLength = ByteBuffer.wrap(extendedPayloadLength).getLong();
        }

        if (masked) {
            maskingKey = new byte[4];
            inputStream.read(maskingKey);
        }

        payloadData = new byte[(int) payloadLength];
        inputStream.read(payloadData);

        if (masked) {
            for (int i = 0; i < payloadLength; i++) {
                payloadData[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
            }
        }
    }

    public boolean isFin() {
        return fin;
    }

    public int getOpcode() {
        return opcode;
    }

    public byte[] getPayloadData() {
        return payloadData;
    }
}
