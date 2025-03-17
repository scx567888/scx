package cool.scx.http.headers.content_type;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;
import cool.scx.http.media_type.ScxMediaType;

import java.util.regex.Pattern;

/// ContentTypeHelper
///
/// @author scx567888
/// @version 0.0.1
public class ContentTypeHelper {

    public static final Pattern SEMICOLON_PATTERN = Pattern.compile(";\\s*");

    public static ContentTypeWritable decodedContentType(String contentTypeStr) {
        if (contentTypeStr == null) {
            return null;
        }
        var parts = SEMICOLON_PATTERN.split(contentTypeStr);
        if (parts.length == 0) {
            return null;
        }
        var mediaType = ScxMediaType.of(parts[0]);
        ParametersWritable<String, String> params = Parameters.of();
        for (var i = 1; i < parts.length; i = i + 1) {
            var s = parts[i].split("=", 2);
            if (s.length == 2) {
                params.add(s[0], s[1]);
            }
        }
        return new ContentTypeImpl().mediaType(mediaType).params(params);
    }

    public static String encodeContentType(ContentTypeImpl contentType) {
        var mediaType = contentType.mediaType();
        var params = contentType.params();
        var sb = new StringBuilder(mediaType.value());
        if (params != null) {
            encodeParams(sb, params);
        }
        return sb.toString();
    }

    public static void encodeParams(StringBuilder result, Parameters<String, String> params) {
        for (var v : params) {
            var key = v.getKey();
            var values = v.getValue();
            for (var value : values) {
                result.append("; ").append(key).append("=").append(value);
            }
        }
    }

}
