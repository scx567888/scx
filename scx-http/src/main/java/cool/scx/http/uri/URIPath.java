package cool.scx.http.uri;

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

    /**
     * 未解码的值
     *
     * @return v
     */
    String rawValue();

}
