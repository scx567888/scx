package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.ByteInput;

public interface Http1UpgradeHandler {

    boolean canHandle(ScxUpgrade scxUpgrade);

    ScxHttpServerRequest createScxHttpServerRequest(Http1ServerConnection connection, Http1RequestLine requestLine, Http1Headers headers, ByteInput bodyByteInput);

}
