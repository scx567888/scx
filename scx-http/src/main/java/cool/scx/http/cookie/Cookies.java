package cool.scx.http.cookie;

public interface Cookies extends Iterable<Cookie> {

    public static CookiesImpl of() {
        return new CookiesImpl();
    }

    //todo 这里需要使用 cookie 解析器
    public static CookiesImpl of(String cookieStr) {
        return new CookiesImpl();
    }

    long size();

    Cookie get(String name);

}
