package cool.scx.http.headers.cookie;

import cool.scx.common.multi_map.MultiMap;

import java.util.Iterator;

/// CookiesImpl
///
/// @author scx567888
/// @version 0.0.1
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
        return cookies.get(name);
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
        cookies.add(cookie.name(), cookie);
        return this;
    }

}
