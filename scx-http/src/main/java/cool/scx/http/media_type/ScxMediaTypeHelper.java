package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;

import java.util.regex.Pattern;

public class ScxMediaTypeHelper {

    public static final Pattern SEMICOLON_PATTERN = Pattern.compile(";\\s*");

    public static ScxMediaTypeWritable decodedMediaType(String str) throws IllegalMediaTypeException {
        var parts = SEMICOLON_PATTERN.split(str);

        //处理意外情况
        if (parts.length == 0) {
            throw new IllegalMediaTypeException(str);
        }

        var mediaTypeStr = parts[0];
        var split = mediaTypeStr.split("/");
        if (split.length != 2) {
            throw new IllegalMediaTypeException(str);
        }

        var m = new ScxMediaTypeImpl();

        m.type(split[0]);
        m.subtype(split[1]);

        for (var i = 1; i < parts.length; i = i + 1) {
            var s = parts[i].split("=", 2);
            if (s.length == 2) {
                m.params().add(s[0], s[1]);
            }
        }

        return m;
    }

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
