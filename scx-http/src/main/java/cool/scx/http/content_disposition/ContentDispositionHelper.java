package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import java.util.ArrayList;

import static cool.scx.common.util.StringUtils.removeQuotes;

public class ContentDispositionHelper {

    public static ContentDispositionWritable decodedContentDisposition(String contentDispositionStr) {
        if (contentDispositionStr == null) {
            return null;
        }
        var split = contentDispositionStr.split(";\\s*");
        if (split.length == 0) {
            return null;
        }
        var type = split[0];
        ParametersWritable<String, String> params = Parameters.of();
        for (var i = 1; i < split.length; i = i + 1) {
            var s = split[i].split("=", 2);
            if (s.length == 2) {
                //移除两端的引号
                params.add(s[0], removeQuotes(s[1]));
            }
        }
        return new ContentDispositionImpl().type(type).params(params);
    }

    public static String encodeContentDisposition(ContentDisposition contentType) {
        var sb = new StringBuilder();
        var type = contentType.type();
        var params = contentType.params();
        sb.append(type);
        if (contentType.params() != null && !contentType.params().isEmpty()) {
            sb.append("; ");
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
