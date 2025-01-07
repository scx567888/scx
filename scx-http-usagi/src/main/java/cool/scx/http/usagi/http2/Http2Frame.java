package cool.scx.http.usagi.http2;

public record Http2Frame(int length, Http2FrameType type, byte flags, int streamId) {

}
