package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxMediaType;

import java.util.regex.Pattern;

public class ContentTypeHelper {

    public static final Pattern SEMICOLON_PATTERN = Pattern.compile(";\\s*");
    public static final Pattern EQUALS_SIGN_PATTERN = Pattern.compile("=");

    public static ContentTypeWritable decodedContentType(String contentTypeStr) {
        if (contentTypeStr == null) {
            return null;
        }
        var split = SEMICOLON_PATTERN.split(contentTypeStr);
        if (split.length == 0) {
            return null;
        }
        var mediaType = ScxMediaType.of(split[0]);
        ParametersWritable<String, String> params = Parameters.of();
        for (var i = 1; i < split.length; i = i + 1) {
            var s = EQUALS_SIGN_PATTERN.split(split[i], 2);
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
