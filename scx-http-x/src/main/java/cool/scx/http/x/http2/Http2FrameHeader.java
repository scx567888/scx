package cool.scx.http.x.http2;

public record Http2FrameHeader(int length, Http2FrameType type, byte flags, int streamId) {

    public static Http2FrameHeader of(byte[] data) {
        return Http2FrameHeaderHelper.parseHttp2FrameHeader(data);
    }
    
}
