package cool.scx.http.usagi;

import cool.scx.http.WebSocketOpCode;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc6455">https://www.rfc-editor.org/rfc/rfc6455</a>
 */
public record WebSocketFrame(boolean fin,
                             WebSocketOpCode opCode,
                             boolean masked,
                             int payloadLength,
                             byte[] maskingKey,
                             byte[] payloadData) {

    public WebSocketFrame(boolean fin, WebSocketOpCode opCode, byte[] payloadData) {
        this(fin, opCode, false, payloadData.length, null, payloadData);
    }

}
