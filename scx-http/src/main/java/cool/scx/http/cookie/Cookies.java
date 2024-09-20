package cool.scx.http.cookie;

public interface Cookies extends Iterable<Cookie> {

    long size();

    Cookie get(String name);

}
