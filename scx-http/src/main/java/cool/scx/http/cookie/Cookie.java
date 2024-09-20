package cool.scx.http.cookie;

public interface Cookie {

    static CookieWritable of(String name, String value) {
        return new CookieImpl(name, value);
    }

    String name();

    String value();

    String domain();

    String path();

    long maxAge();

    boolean secure();

    boolean httpOnly();

    CookieSameSite sameSite();

}
