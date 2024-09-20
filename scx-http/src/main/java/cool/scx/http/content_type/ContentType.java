package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ScxMediaType;

import java.nio.charset.Charset;

public interface ContentType {

    static ContentTypeWritable of(String contentTypeStr) {
        if (contentTypeStr == null) {
            return null;
        }
        var split = contentTypeStr.split(";");
        if (split.length == 0) {
            return null;
        }
        var mediaType = ScxMediaType.of(split[0]);
        var params = Parameters.of();
        for (var i = 1; i < split.length; i = i + 1) {
            var s = split[i].split("=");
            if (s.length == 2) {
                params.add(s[0], s[1]);
            }
        }
        return new ContentTypeImpl().mediaType(mediaType).params(params);
    }

    ScxMediaType mediaType();

    Parameters params();

    default Charset charSet() {
        return Charset.forName(params().get("charset"));
    }

}
