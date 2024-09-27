package cool.scx.http.cookie;

import cool.scx.common.util.MultiMap;

import java.util.Iterator;

public class CookiesImpl implements CookiesWritable {

    private final MultiMap<String, Cookie> cookies;

    public CookiesImpl() {
        this.cookies = new MultiMap<>();
    }

    @Override
    public long size() {
        return cookies.size();
    }

    @Override
    public Cookie get(String name) {
        return cookies.getFirst(name);
    }

    @Override
    public Iterator<Cookie> iterator() {
        return cookies.values().iterator();
    }

    @Override
    public CookiesWritable remove(String name) {
        cookies.removeAll(name);
        return this;
    }

    @Override
    public CookiesWritable add(Cookie cookie) {
        cookies.put(cookie.name(), cookie);
        return this;
    }

}
