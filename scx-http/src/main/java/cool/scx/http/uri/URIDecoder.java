package cool.scx.http.uri;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class URIDecoder {

    public static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
    
}
