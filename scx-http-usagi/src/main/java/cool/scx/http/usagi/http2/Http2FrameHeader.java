package cool.scx.http.usagi.http2;

public record Http2FrameHeader(int length, Http2FrameType type, byte flags, int streamId) {

}
