package cool.scx.http.uri;

/**
 * URIPath
 */
public interface URIPath {

    static URIPathWritable of() {
        return new URIPathImpl();
    }

    static URIPathWritable of(String path) {
        return new URIPathImpl().value(path);
    }

    /**
     * 解码后的值
     *
     * @return v
     */
    String value();

}
