package cool.scx.http.media.form_params;

import java.util.ArrayList;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FormParamsHelper {

    public static String encodeFormParams(FormParams formParams) {
        var l = new ArrayList<String>();
        for (var formParam : formParams) {
            var key = formParam.getKey();
            var values = formParam.getValue();
            for (var value : values) {
                var k = encode(key, UTF_8);
                var v = encode(value, UTF_8);
                l.add(k + "=" + v);
            }
        }
        return String.join("&", l);
    }

    public static FormParams decodeFormParams(String str) {
        var formParams = new FormParams();
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

}
