package cool.scx.http.x.http2;

public record Http2FrameHeader(int length, Http2FrameType type, byte flags, int streamId) {

}
