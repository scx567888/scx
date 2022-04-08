package cool.scx.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class URIBuilder {

    private final URI oldUri;

    private final Map<String, String> queryParam = new HashMap<>();

    public URIBuilder(String uriStr) {
        this.oldUri = URI.create(uriStr);
    }

    public URIBuilder queryParam(String key, Object value) {
        queryParam.put(key, value.toString());
        return this;
    }

    private String getQueryParamStr() {
        var tempList = new ArrayList<String>();
        for (var e : queryParam.entrySet()) {
            tempList.add(e.getKey() + "=" + e.getValue());
        }
        return String.join("&", tempList);
    }

    public URI toURI() {
        //旧的查询
        var oldQuery = oldUri.getQuery();
        //新的
        var newQuery = getQueryParamStr();
        //最终的
        var finalQuery = "";

        if (StringUtils.isBlank(newQuery)) { //新查询为空 直接返回旧查询
            finalQuery = oldQuery;
        } else if (StringUtils.isBlank(oldQuery)) {//旧查询为空 返回新查询
            finalQuery = newQuery;
        } else {//两者都不为空 拼接一下
            finalQuery = oldQuery + "&" + newQuery;
        }

        try {
            return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), finalQuery, oldUri.getFragment());
        } catch (URISyntaxException x) {
            throw new IllegalArgumentException(x.getMessage(), x);
        }
    }

    @Override
    public String toString() {
        return toURI().toString();
    }

}