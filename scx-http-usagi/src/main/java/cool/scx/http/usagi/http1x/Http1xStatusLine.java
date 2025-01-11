package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;

public record Http1xStatusLine(HttpVersion version, int code, String reason) {

    public static Http1xStatusLine of(String statusLineStr) {
        return Http1xStatusLineHelper.parseStatusLine(statusLineStr);
    }
    
}
