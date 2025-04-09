package cool.scx.http.headers;

/// ScxHttpHeadersHelper
///
/// @author scx567888
/// @version 0.0.1
public final class ScxHttpHeadersHelper {

    /**
     * 解析 header 头
     *
     * @param headers    头
     * @param headersStr 文本
     * @param strictMode 严格模式 使用严格模式 则只会按照 \r\n 进行分割 否则 会同时兼容 \r\n 和 \n
     * @param <T>        T
     * @return 头
     */
    public static <T extends ScxHttpHeadersWritable> T parseHeaders(T headers, String headersStr, boolean strictMode) {
        var length = headersStr.length();
        var start = 0;

        // 使用 indexOf("\r\n") 查找每行的结束位置
        while (start < length) {
            // 查找 \n 的位置
            var end = headersStr.indexOf('\n', start);

            // 如果没有找到 \n 说明到达字符串末尾
            if (end == -1) {
                end = length;
            }

            // 查找 冒号的 索引
            var colonIndex = headersStr.indexOf(':', start, end);
            //没有 : 直接跳过
            if (colonIndex != -1) {
                var keyStart = start;
                var keyEnd = colonIndex;
                var valueStart = colonIndex + 1;
                var valueEnd = end;

                //这里需要处理 keyStart 去除前面的空格 
                while (keyStart < keyEnd && isWhitespace(headersStr.charAt(keyStart))) {
                    keyStart = keyStart + 1;
                }
                //这里需要处理 keyEnd 去除后边空格
                while (keyEnd > keyStart && isWhitespace(headersStr.charAt(keyEnd - 1))) {
                    keyEnd = keyEnd - 1;
                }
                //这里需要处理 valueStart 去除前面的空格
                while (valueStart < valueEnd && isWhitespace(headersStr.charAt(valueStart))) {
                    valueStart = valueStart + 1;
                }
                //这里需要处理 valueEnd 去除末尾的 '\r' 如果有 同时需要去除空格
                if (headersStr.charAt(valueEnd - 1) == '\r') {
                    valueEnd = valueEnd - 1;
                }
                while (valueEnd > valueStart && isWhitespace(headersStr.charAt(valueEnd - 1))) {
                    valueEnd = valueEnd - 1;
                }

                var key = headersStr.substring(keyStart, keyEnd);
                var value = headersStr.substring(valueStart, valueEnd);
                headers.add(key, value);
            }

            // 跳到下一行的开始位置
            start = end + 1;  // +1 跳过 "\n"
        }

        return headers;
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t';
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
