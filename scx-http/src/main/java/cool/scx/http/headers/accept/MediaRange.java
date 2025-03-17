package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.parameters.Parameters;

/// MediaRange
///
/// @author scx567888
/// @version 0.0.1
public interface MediaRange {

    static MediaRangeWritable of(String mediaRangeStr) {
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

    // 判断是否匹配某个 MediaType
    default boolean matches(ScxMediaType mediaType) {
        boolean typeMatch = type().equals("*") || type().equals(mediaType.type());
        boolean subtypeMatch = subtype().equals("*") || subtype().equals(mediaType.subtype());
        return typeMatch && subtypeMatch;
    }

}
