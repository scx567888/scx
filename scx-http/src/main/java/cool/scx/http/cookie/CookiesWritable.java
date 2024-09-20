package cool.scx.http.cookie;

public interface CookiesWritable extends Cookies{

    CookiesWritable add(Cookie cookie);

    CookiesWritable remove(String name);
    
}
