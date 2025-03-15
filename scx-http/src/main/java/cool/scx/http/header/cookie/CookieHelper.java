package cool.scx.http.header.cookie;

import java.util.ArrayList;

import static cool.scx.http.header.content_type.ContentTypeHelper.SEMICOLON_PATTERN;


/// CookieHelper
/// todo 中文编码有一些问题
///
/// @author scx567888
/// @version 0.0.1
public class CookieHelper {

    /// 注意 用来解析 cookie 不是 setCookie
    ///
    /// @param cookieStr c
    /// @return c
    public static CookiesImpl parseCookies(String cookieStr) {
        var cookies = new CookiesImpl();
        if (cookieStr != null) {
            var c = SEMICOLON_PATTERN.split(cookieStr);
            for (var cookie : c) {
                String[] parts = cookie.split("=", 2);
                if (parts.length == 2) {
                    cookies.add(Cookie.of(parts[0], parts[1]));
                }
            }
        }
        return cookies;
    }

    public static Cookie parseSetCookie(String setCookieHeader) {
        //1, 分割先
        var parts = SEMICOLON_PATTERN.split(setCookieHeader);
        //2, 获取 name 和 value
        var nameValue = parts[0].split("=", 2);
        var name = nameValue[0];
        var value = nameValue.length > 1 ? nameValue[1] : "";

        var cookie = new CookieImpl(name, value);
        //3, 其余参数
        for (int i = 1; i < parts.length; i = i + 1) {
            var attr = parts[i].split("=", 2);
            var key = attr[0];
            var attrValue = attr.length > 1 ? attr[1] : "";

            // 解析其他属性
            if (key.equalsIgnoreCase("Domain")) {
                cookie.domain(attrValue);
            } else if (key.equalsIgnoreCase("Path")) {
                cookie.path(attrValue);
            } else if (key.equalsIgnoreCase("Max-Age")) {
                cookie.maxAge(Long.parseLong(attrValue));
            } else if (key.equalsIgnoreCase("SameSite")) {
                cookie.sameSite(CookieSameSite.find(attrValue));
            } else if (key.equalsIgnoreCase("Secure")) {
                cookie.secure(true);
            } else if (key.equalsIgnoreCase("HttpOnly")) {
                cookie.httpOnly(true);
            }
        }

        return cookie;
    }

    public static CookiesImpl parseSetCookie(String[] setCookies) {
        var c = new CookiesImpl();
        for (var str : setCookies) {
            var cookies = parseSetCookie(str);
            c.add(cookies);
        }
        return c;
    }

    public static String encodeCookie(Cookie cookie) {
        var buf = new StringBuilder()
                .append(cookie.name())
                .append('=')
                .append(cookie.value());
        if (cookie.domain() != null) {
            buf.append("; Domain=")
                    .append(cookie.domain());
        }
        if (cookie.path() != null) {
            buf.append("; Path=")
                    .append(cookie.path());
        }
        if (cookie.maxAge() != null) {
            buf.append("; Max-Age=")
                    .append(cookie.maxAge());
        }
        if (cookie.sameSite() != null) {
            buf.append("; SameSite=")
                    .append(cookie.sameSite().value());
        }
        if (cookie.secure()) {
            buf.append("; Secure");
        }
        if (cookie.httpOnly()) {
            buf.append("; HttpOnly");
        }
        return buf.toString();
    }

    public static String encodeCookie(Cookies cookies) {
        var sb = new ArrayList<String>();
        for (Cookie cookie : cookies) {
            String s = encodeCookie(cookie);
            sb.add(s);
        }
        return String.join("; ", sb);
    }

    public static String[] encodeSetCookie(Cookies cookies) {
        var s = new ArrayList<String>();
        for (Cookie cookie : cookies) {
            var s1 = encodeCookie(cookie);
            s.add(s1);
        }
        return s.toArray(String[]::new);
    }

}
