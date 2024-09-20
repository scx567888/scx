package cool.scx.http.cookie;

public interface Cookie {

    static CookieWritable of() {
        return new CookieImpl();
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
