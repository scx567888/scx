package cool.scx.http;

public class ScxHttpHeadersHelper {

    public static ScxHttpHeadersWritable parseHeaders(String headersStr) {
        var headers = new ScxHttpHeadersImpl();
        var lines = headersStr.split("\r\n");

        for (var line : lines) {
            int i = line.indexOf(":");
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
