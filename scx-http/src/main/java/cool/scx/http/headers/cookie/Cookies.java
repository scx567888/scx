package cool.scx.http.headers.cookie;

import java.util.List;

/// Cookies
///
/// @author scx567888
/// @version 0.0.1
public interface Cookies extends Iterable<Cookie> {

    static CookiesImpl of() {
        return new CookiesImpl();
    }

    static CookiesImpl of(Cookies oldCookies) {
        var c = new CookiesImpl();
        oldCookies.forEach(c::add);
        return c;
    }

    /// cookie 头
    ///
    /// @param cookieStr c
    /// @return c
    static CookiesImpl of(String cookieStr) {
        return CookieHelper.parseCookies(cookieStr);
    }

    /// set-cookie 头
    ///
    /// @param setCookieStrList c
    /// @return c
    static CookiesImpl ofSetCookie(List<String> setCookieStrList) {
        return CookieHelper.parseSetCookie(setCookieStrList);
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
