package cool.scx.socket_vertx;

import cool.scx.common.multi_map.MultiMap;
import cool.scx.common.util.StringUtils;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.QueryStringEncoder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * URIBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
final class URIBuilder {

    /**
     * 切割正则表达式
     */
    private static final Pattern PATH_SEPARATOR = Pattern.compile("[/\\\\]+");

    private final String path;

    private final MultiMap<String, String> queryParams = new MultiMap<>();

    private URIBuilder(QueryStringDecoder decoder) {
        this.path = decoder.path();
        decoder.parameters().forEach(this.queryParams::add);
    }

    public static URIBuilder of(String str) {
        return new URIBuilder(new QueryStringDecoder(str, StandardCharsets.UTF_8));
    }

    public static URIBuilder of(URI uri) {
        return new URIBuilder(new QueryStringDecoder(uri, StandardCharsets.UTF_8));
    }

    /**
     * 拼接多个 uri 并进行一些简单的清理  例 : 处理前 ["a/b/", "/c"] 处理后 "a/b/c"
     *
     * @param uris 需要清理的 uri 集合
     * @return 拼接后的结果
     */
    public static String join(String... uris) {
        return normalize(String.join("/", Arrays.stream(uris).filter(StringUtils::notBlank).toArray(String[]::new)));
    }

    public static String join(Collection<String> uris) {
        return normalize(String.join("/", uris.stream().filter(StringUtils::notBlank).toArray(String[]::new)));
    }

    /**
     * 移除两端的 "/" 或 "\"
     * 注意不要和 {@link String#trim()} 混淆 此方法不处理空格 只处理斜杠
     *
     * @param uri a
     * @return a
     */
    public static String trimSlash(String uri) {
        var value = uri.toCharArray();
        int length = value.length;
        int st = findSlashStart(value);
        int len = findSlashEnd(value);
        return st > 0 || len < length ? new String(Arrays.copyOfRange(value, st, len)) : uri;
    }

    public static String trimSlashStart(String uri) {
        var value = uri.toCharArray();
        int st = findSlashStart(value);
        return st > 0 ? new String(Arrays.copyOfRange(value, st, value.length)) : uri;
    }

    public static String trimSlashEnd(String uri) {
        var value = uri.toCharArray();
        int len = findSlashEnd(value);
        return len < value.length ? new String(Arrays.copyOfRange(value, 0, len)) : uri;
    }

    private static int findSlashStart(char[] value) {
        int length = value.length;
        int st = 0;
        while (st < length && (value[st] == '/' || value[st] == '\\')) {
            st = st + 1;
        }
        return st;
    }

    private static int findSlashEnd(char[] value) {
        int len = value.length;
        int st = 0;
        while (st < len && (value[len - 1] == '/' || value[len - 1] == '\\')) {
            len--;
        }
        return len;
    }

    public static String addSlashStart(String uri) {
        return "/" + trimSlashStart(uri);
    }

    public static String addSlashEnd(String uri) {
        return trimSlashEnd(uri) + "/";
    }

    /**
     * 将 \ 分割的全部转换为 / 并清除多余的 /
     *
     * @param uri a
     * @return a
     */
    public static String normalize(String uri) {
        var chars = uri.toCharArray();
        var index = 0;
        var isSeparator = false;
        for (var c : chars) {
            if (c == '/' || c == '\\') {
                if (!isSeparator) {
                    chars[index] = '/';
                    index = index + 1;
                }
                isSeparator = true;
            } else {
                chars[index] = c;
                index = index + 1;
                isSeparator = false;
            }
        }
        return new String(chars, 0, index);
    }

    public static String[] split(String uri) {
        return PATH_SEPARATOR.split(uri, -1);
    }

    public URIBuilder addParam(String key, Object value) {
        queryParams.add(key, value.toString());
        return this;
    }

    public URIBuilder removeParam(String key) {
        queryParams.removeAll(key);
        return this;
    }

    public List<String> getParams(String key) {
        return queryParams.getAll(key);
    }

    public Map<String, List<String>> getAllParams() {
        return queryParams.toMultiValueMap();
    }

    public URIBuilder removeAllParams() {
        queryParams.clear();
        return this;
    }

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
