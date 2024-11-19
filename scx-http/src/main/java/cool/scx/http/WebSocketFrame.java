package cool.scx.http;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public record WebSocketFrame(boolean fin,
                             boolean rsv1,
                             boolean rsv2,
                             boolean rsv3,
                             WebSocketOpCode opCode,
                             boolean masked,
                             long payloadLength,
                             byte[] maskingKey,
                             byte[] payloadData) {

    public static WebSocketFrame of(boolean fin, WebSocketOpCode opCode, byte[] maskingKey, byte[] payloadData) {
        return new WebSocketFrame(fin, false, false, false, opCode,maskingKey != null, payloadData.length, maskingKey, payloadData);
    }

    public static WebSocketFrame of(boolean fin, WebSocketOpCode opCode, byte[] payloadData) {
        return of(fin, opCode, null, payloadData);
    }

    public static WebSocketFrame of(WebSocketOpCode opCode, byte[] payloadData) {
        return of(true, opCode, payloadData);
    }

}
