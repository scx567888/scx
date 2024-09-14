package cool.scx.http.uri;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URIEncoder {

    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
    
}
