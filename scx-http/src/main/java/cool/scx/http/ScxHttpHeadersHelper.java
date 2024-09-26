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
    
}
