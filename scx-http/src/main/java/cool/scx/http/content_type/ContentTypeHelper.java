package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxMediaType;

import java.util.ArrayList;

public class ContentTypeHelper {

    public static ContentTypeWritable decodedContentType(String contentTypeStr) {
        if (contentTypeStr == null) {
            return null;
        }
        var split = contentTypeStr.split(";");
        if (split.length == 0) {
            return null;
        }
        var mediaType = ScxMediaType.of(split[0]);
        ParametersWritable<String, String> params = Parameters.of();
        for (var i = 1; i < split.length; i = i + 1) {
            var s = split[i].split("=");
            if (s.length == 2) {
                params.add(s[0], s[1]);
            }
        }
        return new ContentTypeImpl().mediaType(mediaType).params(params);
    }

    public static String encodeContentType(ContentTypeImpl contentType) {
        var sb = new StringBuilder();
        var mediaType = contentType.mediaType();
        var params = contentType.params();
        sb.append(mediaType.value());
        if (contentType.params() != null && !contentType.params().isEmpty()) {
            sb.append(';');
            sb.append(encodeParams(params));
        }
        return sb.toString();
    }

    public static String encodeParams(Parameters<String, String> params) {
        var l = new ArrayList<String>();
        for (var v : params) {
            var key = v.getKey();
            var value = v.getValue();
            for (var s : value) {
                l.add(key + "=" + s);
            }
        }
        return String.join(";", l);
    }

}
