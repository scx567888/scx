package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.parameters.Parameters;

/// MediaRange
///
/// @author scx567888
/// @version 0.0.1
public interface MediaRange {

    static MediaRangeWritable of(String mediaRangeStr) throws IllegalMediaRangeException {
        return MediaRangeHelper.parseMediaRange(mediaRangeStr);
    }

    static MediaRangeWritable of() {
        return new MediaRangeImpl();
    }

    String type();

    String subtype();

    Parameters<String, String> params();

    default Double q() {
        var s = params().get("q");
        return s != null ? Double.parseDouble(s) : 1.0;
    }

    /// 判断是否匹配某个 MediaType 支持通配符匹配
    default boolean matches(ScxMediaType mediaType) {
        var typeMatch = type().equals("*") || type().equals(mediaType.type());
        var subtypeMatch = subtype().equals("*") || subtype().equals(mediaType.subtype());
        return typeMatch && subtypeMatch;
    }

    /// 判断是否匹配某个 MediaType 严格匹配 忽略通配符
    default boolean exactMatch(ScxMediaType mediaType) {
        var typeMatch = type().equals(mediaType.type());
        var subtypeMatch = subtype().equals(mediaType.subtype());
        return typeMatch && subtypeMatch;
    }

}
