package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;

public class Http1xStatusLineHelper {

    public static Http1xStatusLine parseStatusLine(String statusLineStr) {
        var parts = statusLineStr.split(" ", 3);

        if (parts.length != 3) {
            throw new RuntimeException("Invalid status line: " + statusLineStr);
        }

        var versionStr = parts[0];
        var codeStr = parts[1];
        var reasonStr = parts[2];

        var version = HttpVersion.of(versionStr);
        var code = Integer.parseInt(codeStr);

        return new Http1xStatusLine(version, code, reasonStr);
    }

}
