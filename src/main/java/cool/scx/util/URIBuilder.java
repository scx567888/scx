package cool.scx.util;

import com.google.common.collect.ArrayListMultimap;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.QueryStringEncoder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * a
 */
public final class URIBuilder {

    /**
     * a
     */
    private final String path;

    /**
     * a
     */
    private final ArrayListMultimap<String, String> queryParams = ArrayListMultimap.create();

    /**
     * a
     *
     * @param decoder a
     */
    private URIBuilder(QueryStringDecoder decoder) {
        this.path = decoder.path();
        decoder.parameters().forEach(this.queryParams::putAll);
    }

    /**
     * a
     *
     * @param str a
     * @return a
     */
    public static URIBuilder of(String str) {
        return new URIBuilder(new QueryStringDecoder(str, StandardCharsets.UTF_8));
    }

    /**
     * a
     *
     * @param uri a
     * @return a
     */
    public static URIBuilder of(URI uri) {
        return new URIBuilder(new QueryStringDecoder(uri, StandardCharsets.UTF_8));
    }

    /**
     * 清理多个 uri 并拼接 清理前 : ["a/b/", "/c"] -- 清理后 : "/a/b/c"
     *
     * @param uris 需要清理的 uri 集合
     * @return 拼接后的结果
     */
    public static String join(String... uris) {
        return Arrays.stream(String.join("/", uris).split("/")).filter(StringUtils::notBlank).collect(Collectors.joining("/", "/", ""));
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
        var encoder = new QueryStringEncoder(path, StandardCharsets.UTF_8);
        queryParams.forEach(encoder::addParam);
        return encoder.toString();
    }

}
