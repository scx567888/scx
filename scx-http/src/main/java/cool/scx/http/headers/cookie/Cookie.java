package cool.scx.http.headers.cookie;

/// Cookie
///
/// @author scx567888
/// @version 0.0.1
public interface Cookie {

    static CookieWritable of(String name, String value) {
        return new CookieImpl(name, value);
    }

    String name();

    String value();

    String domain();

    String path();

    Long maxAge();

    CookieSameSite sameSite();

    boolean secure();

    boolean httpOnly();

    String encode();

}
