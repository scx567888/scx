package cool.scx.http.header.cookie;

/// CookieWritable
///
/// @author scx567888
/// @version 0.0.1
public interface CookieWritable extends Cookie {

    CookieWritable domain(String domain);

    CookieWritable path(String path);

    CookieWritable maxAge(Long maxAge);

    CookieWritable sameSite(CookieSameSite sameSite);

    CookieWritable secure(boolean secure);

    CookieWritable httpOnly(boolean httpOnly);

}
