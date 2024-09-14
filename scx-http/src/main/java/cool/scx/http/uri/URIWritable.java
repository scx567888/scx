package cool.scx.http.uri;

public interface URIWritable extends URI {

    URIWritable scheme(String scheme);

    URIWritable host(String host);

    URIWritable port(int port);

    URIWritable path(URIPath path);

    URIWritable query(URIQuery query);

    URIWritable fragment(URIFragment fragment);


}
