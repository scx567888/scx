package cool.scx.http.media.form_params;

import java.util.ArrayList;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

/// FormParamsHelper
///
/// @author scx567888
/// @version 0.0.1
public final class FormParamsHelper {

    public static <T extends FormParamsWritable> T decodeFormParams(T formParams, String str) {
        var pairs = str.split("&");
        for (var pair : pairs) {
            var keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                var key = decode(keyValue[0], UTF_8);
                var value = decode(keyValue[1], UTF_8);
                formParams.add(key, value);
            }
        }
        return formParams;
    }

    public static String encodeFormParams(FormParams formParams) {
        var l = new ArrayList<String>();
        for (var formParam : formParams) {
            var key = formParam.name();
            var values = formParam.values();
            for (var value : values) {
                var k = encode(key, UTF_8);
                var v = encode(value, UTF_8);
                l.add(k + "=" + v);
            }
        }
        return String.join("&", l);
    }

}
