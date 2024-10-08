package cool.scx.http.cookie;

public interface CookieWritable extends Cookie {

    CookieWritable domain(String domain);

    CookieWritable path(String path);

    CookieWritable maxAge(Long maxAge);

    CookieWritable sameSite(CookieSameSite sameSite);

    CookieWritable secure(boolean secure);

    CookieWritable httpOnly(boolean httpOnly);

}
