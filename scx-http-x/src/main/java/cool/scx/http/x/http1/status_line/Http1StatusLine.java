package cool.scx.http.x.http1.status_line;

import cool.scx.http.version.HttpVersion;

public record Http1StatusLine(HttpVersion version, int code, String reason) {

    public static Http1StatusLine of(String statusLineStr) {
        return Http1StatusLineHelper.parseStatusLine(statusLineStr);
    }

    public String encode() {
        return Http1StatusLineHelper.encode(this);
    }

}
