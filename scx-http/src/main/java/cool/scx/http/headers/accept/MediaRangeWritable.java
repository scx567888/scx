package cool.scx.http.headers.accept;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;

/// MediaRangeWritable
///
/// @author scx567888
/// @version 0.0.1
public interface MediaRangeWritable extends MediaRange {

    MediaRangeWritable type(String type);

    MediaRangeWritable subtype(String subtype);

    MediaRangeWritable params(Parameters<String, String> params);

    MediaRangeWritable q(Double q);

    @Override
    ParametersWritable<String, String> params();

}
