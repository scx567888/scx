package cool.scx.http.cookie;

import java.util.Iterator;

//todo 
public class CookiesImpl implements CookiesWritable, Iterator<Cookie> {

    @Override
    public long size() {
        return 0;
    }

    @Override
    public Cookie get(String name) {
        return null;
    }

    @Override
    public Iterator<Cookie> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Cookie next() {
        return null;
    }

    @Override
    public CookiesWritable remove(String name) {
        return null;
    }

    @Override
    public CookiesWritable add(Cookie cookie) {
        return null;
    }
    
}
