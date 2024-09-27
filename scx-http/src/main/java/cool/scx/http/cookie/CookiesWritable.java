package cool.scx.http.cookie;

public interface CookiesWritable extends Cookies {

    CookiesWritable remove(String name);

    CookiesWritable add(Cookie cookie);
    
}
