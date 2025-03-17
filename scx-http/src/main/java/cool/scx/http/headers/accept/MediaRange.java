package cool.scx.http.headers.accept;

import cool.scx.http.parameters.Parameters;

/// MediaRange
///
/// @author scx567888
/// @version 0.0.1
public interface MediaRange {

    static MediaRangeWritable of() {
        return new MediaRangeImpl();
    }

    String type();

    String subtype();

    Parameters<String, String> params();

    Double q();

}
