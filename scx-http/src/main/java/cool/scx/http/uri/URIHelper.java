package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import java.util.ArrayList;

/**
 * URIHelper
 */
public class URIHelper {

    public static ParametersWritable<String, String> decodeQuery(String value) {
        ParametersWritable<String, String> query = Parameters.of();
        if (value == null) {
            return query;
        }
        var split = value.split("&");
        for (var s : split) {
            split = s.split("=", 2);
            if (split.length == 2) {
                query.add(split[0], split[1]);
            }
        }
        return query;
    }

    public static String toString(Parameters<String, String> query) {
        var l = new ArrayList<String>();
        for (var v : query) {
            var key = v.getKey();
            var value = v.getValue();
            for (String s : value) {
                l.add(key + "=" + s);
            }
        }
        return String.join("&", l);
    }

    public static String toString(ScxURI uri) {
        var scheme = uri.scheme();
        var host = uri.host();
        var port = uri.port();
        var path = uri.path();
        var query = uri.query();
        var fragment = uri.fragment();
        var sb = new StringBuilder();
        if (scheme != null) {
            sb.append(scheme);
            sb.append(':');
        }

        if (host != null) {
            sb.append("//");

            boolean needBrackets = ((host.indexOf(':') >= 0)
                                    && !host.startsWith("[")
                                    && !host.endsWith("]"));
            if (needBrackets) {
                sb.append('[');
            }
            sb.append(host);
            if (needBrackets) {
                sb.append(']');
            }
            if (port != -1) {
                sb.append(':');
                sb.append(port);
            }
        }

        if (path != null && path.value() != null) {
            sb.append(path.value());
        }

        if (query != null && !query.isEmpty()) {
            sb.append('?');
            sb.append(toString(query));
        }

        if (fragment != null && fragment.value() != null) {
            sb.append('#');
            sb.append(fragment.value());
        }
        return sb.toString();
    }

}
