package cool.scx.http.cookie;

public interface CookieWritable extends Cookie {

    CookieWritable name(String name);

    CookieWritable value(String value);

    CookieWritable domain(String domain);

    CookieWritable path(String path);

    CookieWritable maxAge(long maxAge);

    CookieWritable secure(boolean secure);

    CookieWritable httpOnly(boolean httpOnly);

    CookieWritable sameSite(CookieSameSite sameSite);
    
}
