package cool.scx.http.uri;

public interface URIPathWritable extends URIPath {

    URIPathWritable value(String value);
    
    URIPathWritable rawValue(String rawValue);
    
}
