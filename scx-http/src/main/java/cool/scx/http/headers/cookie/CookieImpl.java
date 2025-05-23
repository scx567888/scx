package cool.scx.http.headers.cookie;

import static cool.scx.http.headers.cookie.CookieHelper.encodeCookie;

/// CookieImpl
///
/// @author scx567888
/// @version 0.0.1
class CookieImpl implements CookieWritable {

    private final String name;
    private final String value;
    private String domain;
    private String path;
    private Long maxAge;
    private boolean secure;
    private boolean httpOnly;
    private CookieSameSite sameSite;

    public CookieImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public CookieWritable domain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public CookieWritable path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public CookieWritable maxAge(Long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    @Override
    public CookieWritable secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    @Override
    public CookieWritable httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    @Override
    public CookieWritable sameSite(CookieSameSite sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String domain() {
        return domain;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public Long maxAge() {
        return maxAge;
    }

    @Override
    public boolean secure() {
        return secure;
    }

    @Override
    public boolean httpOnly() {
        return httpOnly;
    }

    @Override
    public CookieSameSite sameSite() {
        return sameSite;
    }

    @Override
    public String encode() {
        return encodeCookie(this);
    }

    @Override
    public String toString() {
        return encode();
    }

}
