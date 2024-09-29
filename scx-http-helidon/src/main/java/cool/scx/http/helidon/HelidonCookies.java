package cool.scx.http.helidon;

import cool.scx.http.cookie.Cookie;
import cool.scx.http.cookie.Cookies;
import io.helidon.common.parameters.Parameters;

import java.util.Iterator;

public class HelidonCookies implements Cookies {

    private final Parameters cookies;

    public HelidonCookies(Parameters cookies) {
        this.cookies = cookies;
    }

    @Override
    public long size() {
        return cookies.size();
    }

    @Override
    public Cookie get(String name) {
        var value = cookies.get(name);
        if (value == null) {
            return null;
        } else {
            return Cookie.of(name, value);
        }
    }

    @Override
    public Iterator<Cookie> iterator() {
        var itr = cookies.toMap().entrySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return itr.hasNext();
            }

            @Override
            public Cookie next() {
                var next = itr.next();
                var key = next.getKey();
                var value = next.getValue();
                return Cookie.of(key, value.size() > 0 ? value.get(0) : null);
            }
        };
    }

}
