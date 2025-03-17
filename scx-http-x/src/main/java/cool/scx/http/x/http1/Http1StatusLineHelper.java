package cool.scx.http.x.http1;

import cool.scx.http.version.HttpVersion;

public class Http1StatusLineHelper {

    public static Http1StatusLine parseStatusLine(String statusLineStr) {
        var parts = statusLineStr.split(" ", 3);

        if (parts.length != 3) {
            throw new RuntimeException("Invalid status line: " + statusLineStr);
        }

        var versionStr = parts[0];
        var codeStr = parts[1];
        var reasonStr = parts[2];

        var version = HttpVersion.of(versionStr);
        var code = Integer.parseInt(codeStr);

        return new Http1StatusLine(version, code, reasonStr);
    }

}
