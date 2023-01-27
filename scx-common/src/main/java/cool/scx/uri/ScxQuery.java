package cool.scx.uri;

import cool.scx.util.MultiMap;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.QueryStringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ScxQuery {

    /**
     * a
     */
    private final MultiMap<String, String> queryParams = new MultiMap<>();

    public ScxQuery(String str) {
        var decoder = new QueryStringDecoder("?" + str, StandardCharsets.UTF_8);
        decoder.parameters().forEach(this.queryParams::putAll);
    }

    /**
     * a
     *
     * @param key   a
     * @param value a
     * @return a
     */
    public ScxQuery addParam(String key, Object value) {
        queryParams.put(key, value.toString());
        return this;
    }

    /**
     * a
     *
     * @param key a
     * @return a
     */
    public ScxQuery removeParam(String key) {
        queryParams.removeAll(key);
        return this;
    }

    /**
     * a
     *
     * @param key a
     * @return a
     */
    public List<String> getParams(String key) {
        return queryParams.get(key);
    }

    /**
     * a
     *
     * @return a
     */
    public Map<String, List<String>> getAllParams() {
        return queryParams.asMap();
    }

    /**
     * a
     *
     * @return a
     */
    public ScxQuery removeAllParams() {
        queryParams.clear();
        return this;
    }

    @Override
    public String toString() {
        var encoder = new QueryStringEncoder("", StandardCharsets.UTF_8);
        queryParams.forEach(encoder::addParam);
        return encoder.toString().substring(1);
    }

}
