package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;

public class ScxMediaTypeHelper {

    public static String encodeMediaType(ScxMediaType mediaType) {
        var type = mediaType.type();
        var subtype = mediaType.subtype();
        var params = mediaType.params();
        var sb = new StringBuilder().append(type).append('/').append(subtype);
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
