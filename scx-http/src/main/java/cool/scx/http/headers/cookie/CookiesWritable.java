package cool.scx.http.headers.cookie;

/// CookiesWritable
///
/// @author scx567888
/// @version 0.0.1
public interface CookiesWritable extends Cookies {

    CookiesWritable remove(String name);

    CookiesWritable add(Cookie cookie);

}
