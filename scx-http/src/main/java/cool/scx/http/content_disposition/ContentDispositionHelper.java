package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import static cool.scx.common.util.StringUtils.removeQuotes;
import static cool.scx.http.content_type.ContentTypeHelper.SEMICOLON_PATTERN;
import static cool.scx.http.content_type.ContentTypeHelper.encodeParams;

/// ContentDispositionHelper
///
/// @author scx567888
/// @version 0.0.1
public class ContentDispositionHelper {

    public static ContentDispositionWritable decodedContentDisposition(String contentDispositionStr) {
        if (contentDispositionStr == null) {
            return null;
        }
        var parts = SEMICOLON_PATTERN.split(contentDispositionStr);
        if (parts.length == 0) {
            return null;
        }
        var type = parts[0];
        ParametersWritable<String, String> params = Parameters.of();
        for (var i = 1; i < parts.length; i = i + 1) {
            var s = parts[i].split("=", 2);
            if (s.length == 2) {
                //移除两端的引号
                params.add(s[0], removeQuotes(s[1]));
            }
        }
        return new ContentDispositionImpl().type(type).params(params);
    }

    public static String encodeContentDisposition(ContentDisposition contentDisposition) {
        var type = contentDisposition.type();
        var params = contentDisposition.params();
        var sb = new StringBuilder(type);
        if (params != null) {
            encodeParams(sb, params);
        }
        return sb.toString();
    }

}
