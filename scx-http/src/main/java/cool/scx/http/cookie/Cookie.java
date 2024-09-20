package cool.scx.http.cookie;

public interface Cookie {

    String name();

    String value();

    String domain();

    String path();

    long maxAge();

    boolean secure();

    boolean httpOnly();

    CookieSameSite sameSite();
    
}
