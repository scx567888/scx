package cool.scx.http.uri;

public interface URIQueryWritable extends URIQuery {

    URIQueryWritable set(String name, String... value);

    URIQueryWritable add(String name, String... value);

}
