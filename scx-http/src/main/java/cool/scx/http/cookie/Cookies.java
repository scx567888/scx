package cool.scx.http.cookie;

public interface Cookies extends Iterable<Cookie> {

    static CookiesImpl of() {
        return new CookiesImpl();
    }

    /**
     * cookie 头
     *
     * @param cookieStr c
     * @return c
     */
    static CookiesImpl of(String cookieStr) {
        return CookieHelper.parseCookies(cookieStr);
    }

    /**
     * set-cookie 头
     *
     * @param cookieStr c
     * @return c
     */
    static CookiesImpl of(String[] cookieStr) {
        return CookieHelper.parseSetCookie(cookieStr);
    }

    long size();

    Cookie get(String name);

    default String encodeCookie() {
        return CookieHelper.encodeCookie(this);
    }

    default String[] encodeSetCookie() {
        return CookieHelper.encodeSetCookie(this);
    }

}
