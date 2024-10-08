package cool.scx.http.cookie;

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
