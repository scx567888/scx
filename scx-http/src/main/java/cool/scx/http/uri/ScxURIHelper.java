package cool.scx.http.uri;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * URIHelper
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
                    var kk = URLEncoder.encode(key, UTF_8);
                    var vv = URLEncoder.encode(s, UTF_8);
                    l.add(kk + "=" + vv);
                } else {
                    l.add(key + "=" + s);
                }
            }
        }
        return String.join("&", l);
    }

    public static String encodeURI(ScxURI uri, boolean uriEncoding) {
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
                sb.append(URLEncoder.encode(path, UTF_8).replace("%2F", "/"));
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
                sb.append(URLEncoder.encode(fragment, UTF_8));
            } else {
                sb.append(fragment);
            }
        }
        return sb.toString();
    }

    public static URI parseURI(String uriStr) {
        // 预处理 URI，将特殊字符进行编码
        uriStr = URLEncoder.encode(uriStr, UTF_8)
                .replace("%3A", ":")
                .replace("%2F", "/")
                .replace("%3F", "?")
                .replace("%3D", "=")
                .replace("%26", "&")
                .replace("%23", "#");

        // 解析 URI
        return URI.create(uriStr);
    }

}
