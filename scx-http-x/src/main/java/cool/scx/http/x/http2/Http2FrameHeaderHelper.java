package cool.scx.http.x.http2;

public class Http2FrameHeaderHelper {

    public static Http2FrameHeader parseHttp2FrameHeader(byte[] data) {
        int length = (data[0] & 0xFF) << 16 |
                (data[1] & 0xFF) << 8 |
                data[2] & 0xFF;

        var type = Http2FrameType.of(data[3] & 0xFF);

        byte flags = (byte) (data[4] & 0xFF);

        int streamId = (data[5] & 0x7F) << 24 |
                (data[6] & 0xFF) << 16 |
                (data[7] & 0xFF) << 8 |
                data[8] & 0xFF;

        return new Http2FrameHeader(length, type, flags, streamId);
    }

}
