package cool.scx.http.cookie;

public interface Cookies extends Iterable<Cookie> {

    long size();

    String get(String name);

}
