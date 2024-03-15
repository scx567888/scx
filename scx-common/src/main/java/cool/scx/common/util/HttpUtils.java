package cool.scx.common.util;

import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class HttpUtils {

    /**
     * todo 这是一个 hack
     * URLEncoder.encode 针对 ' ' (空格) 会编码为 '+' , 而这里我们需要的是编码为 %20
     *
     * @param downloadName a {@link java.lang.String} object
     * @return c
     */
    public static String getDownloadContentDisposition(String downloadName) {
        return "attachment; filename*=utf-8''" + URLEncoder.encode(downloadName, UTF_8).replace("+", "%20");
    }

}
