package cool.scx.http.headers;

import java.util.regex.Pattern;

/// ScxHttpHeadersHelper 
///
/// @author scx567888
/// @version 0.0.1
public class ScxHttpHeadersHelper {

    public static final Pattern CRLF_PATTERN = Pattern.compile("\r\n");

    public static <T extends ScxHttpHeadersWritable> T parseHeaders(T headers, String headersStr) {
        var lines = CRLF_PATTERN.split(headersStr);

        for (var line : lines) {
            int i = line.indexOf(':');
            if (i != -1) {
                var key = line.substring(0, i).trim();
                var value = line.substring(i + 1).trim();
                headers.add(key, value);
            }
        }

        return headers;
    }

    public static String encodeHeaders(ScxHttpHeaders headers) {
        var sb = new StringBuilder();
        for (var header : headers) {
            var key = header.getKey();
            var values = header.getValue();
            for (var value : values) {
                sb.append(key.value()).append(": ").append(value).append("\r\n");
            }
        }
        return sb.toString();
    }

}
