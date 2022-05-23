package cool.scx.util;

import com.google.common.collect.ArrayListMultimap;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.QueryStringEncoder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * a
 */
public final class URIBuilder {

    /**
     * a
     */
    private final String uri;

    /**
     * a
     */
    private final ArrayListMultimap<String, String> queryParams = ArrayListMultimap.create();

    /**
     * a
     *
     * @param str a
     */
    public URIBuilder(String str) {
        var decoder = new QueryStringDecoder(str, StandardCharsets.UTF_8);
        this.uri = decoder.path();
        var decodedParams = decoder.parameters();
        for (var s : decodedParams.entrySet()) {
            this.queryParams.putAll(s.getKey(), s.getValue());
        }
    }

    /**
     * a
     *
     * @param key   a
     * @param value a
     * @return a
     */
    public URIBuilder addParam(String key, Object value) {
        queryParams.put(key, value.toString());
        return this;
    }

    /**
     * a
     *
     * @param key a
     * @return a
     */
    public URIBuilder removeParam(String key) {
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
    public Map<String, Collection<String>> getAllParams() {
        return queryParams.asMap();
    }

    /**
     * a
     *
     * @return a
     */
    public URIBuilder removeAllParams() {
        queryParams.clear();
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public URI build() {
        return URI.create(this.toString());
    }

    @Override
    public String toString() {
        var encoder = new QueryStringEncoder(uri, StandardCharsets.UTF_8);
        queryParams.forEach(encoder::addParam);
        return encoder.toString();
    }

}