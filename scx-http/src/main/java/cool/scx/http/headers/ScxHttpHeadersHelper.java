package cool.scx.http.headers;

/// ScxHttpHeadersHelper
///
/// @author scx567888
/// @version 0.0.1
public final class ScxHttpHeadersHelper {

    public static <T extends ScxHttpHeadersWritable> T parseHeaders(T headers, String headersStr) {
        var length = headersStr.length();
        var start = 0;

        // 使用 indexOf("\r\n") 查找每行的结束位置
        while (start < length) {
            // 查找 \r\n 的位置
            int end = headersStr.indexOf("\r\n", start);

            // 如果没有找到 \r\n 说明到达字符串末尾
            if (end == -1) {
                break;
            }

            // 进行 key:value 的解析
            int colonIndex = headersStr.indexOf(':', start, end);
            if (colonIndex != -1) {
                var key = headersStr.substring(start, colonIndex).trim();
                var value = headersStr.substring(colonIndex + 1, end).trim();
                headers.add(key, value);
            }

            // 跳到下一行的开始位置
            start = end + 2;  // +2 跳过 "\r\n"
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
