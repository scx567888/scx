package cool.scx.http.x.http1.status_line;

import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.request_line.InvalidHttpVersion;

import static cool.scx.http.version.HttpVersion.HTTP_1_1;

public class Http1StatusLineHelper {

    public static Http1StatusLine parseStatusLine(String statusLineStr) throws InvalidHttpStatusLineException, InvalidHttpVersion, InvalidHttpStatusException {
        var parts = statusLineStr.split(" ", 3);

        if (parts.length != 3) {
            throw new InvalidHttpStatusLineException(statusLineStr);
        }

        var versionStr = parts[0];
        var codeStr = parts[1];
        var reasonStr = parts[2];

        var version = HttpVersion.find(versionStr);

        //这里我们强制 版本号必须是 HTTP/1.1 , 这里需要细化一下 异常
        if (version != HTTP_1_1) {
            throw new InvalidHttpVersion(versionStr);
        }

        int code;
        try {
            code = Integer.parseInt(codeStr);
        } catch (NumberFormatException e) {
            throw new InvalidHttpStatusException(codeStr);
        }

        return new Http1StatusLine(version, code, reasonStr);
    }

    public static String encode(Http1StatusLine statusLine) {
        return statusLine.version().value() + " " + statusLine.code() + " " + statusLine.reason();
    }

}
