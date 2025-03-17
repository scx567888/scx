package cool.scx.http.headers.accept;

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
        return s != null ? Double.parseDouble(s) : null;
    }

}
