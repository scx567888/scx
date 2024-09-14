package cool.scx.http.uri;

public interface URI {

    static URIWritable of() {
        return new URIImpl();
    }
    
    String scheme();

    String host();

    int port();

    URIPath path();

    URIQuery query();

    URIFragment fragment();

}
