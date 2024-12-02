package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import java.util.ArrayList;

import static cool.scx.http.uri.URIEncoder.encodeURIComponent;

/**
 * ScxURIHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxURIHelper {

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

    public static String encodeQuery(Parameters<String, String> query, boolean uriEncoding) {
        var l = new ArrayList<String>();
        for (var v : query) {
            var key = v.getKey();
            var value = v.getValue();
            for (var s : value) {
                if (uriEncoding) {
                    var kk = encodeURIComponent(key);
                    var vv = encodeURIComponent(s);
                    l.add(kk + "=" + vv);
                } else {
                    l.add(key + "=" + s);
                }
            }
        }
        return String.join("&", l);
    }

    public static String encodeScxURI(ScxURI uri, boolean uriEncoding) {
        var scheme = uri.scheme();
        var host = uri.host();
        var port = uri.port();
        var path = uri.path();
        var query = uri.query();
        var fragment = uri.fragment();

        //拼接 scheme
        var sb = new StringBuilder();
        if (scheme != null) {
            sb.append(scheme);
            sb.append(':');
        }

        //拼接 host
        if (host != null) {
            sb.append("//");

            sb.append(host);

            //拼接 端口号
            if (port != -1) {
                sb.append(':');
                sb.append(port);
            }
        }

        //拼接 path
        if (path != null) {
            //是否需要进行 uri 编码
            if (uriEncoding) {
                //我们不编码 "/"
                sb.append(URIEncoder.encodeURI(path));
            } else {
                sb.append(path);
            }
        }

        //拼接查询参数
        if (query != null && !query.isEmpty()) {
            sb.append('?');
            sb.append(encodeQuery(query, uriEncoding));
        }

        if (fragment != null) {
            sb.append('#');
            //是否需要进行 uri 编码
            if (uriEncoding) {
                sb.append(URIEncoder.encodeURI(fragment));
            } else {
                sb.append(fragment);
            }
        }
        return sb.toString();
    }

}
