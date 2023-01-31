package cool.scx.mvc.registrar;

import cool.scx.enumeration.HttpMethod;
import io.vertx.ext.web.MIMEHeader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 用于承载数据
 */
public record RouteState(Map<String, Object> metadata, String path, String name, int order, boolean enabled,
                         Set<HttpMethod> methods, Set<MIMEHeader> consumes, boolean emptyBodyPermittedWithConsumes,
                         Set<MIMEHeader> produces, boolean added, Pattern pattern, List<String> groups,
                         boolean useNormalizedPath, Set<String> namedGroupsInRegex, Pattern virtualHostPattern,
                         boolean pathEndsWithSlash, boolean exclusive, boolean exactPath) {

    int getGroupsOrder() {
        return this.groups == null ? 0 : this.groups.size();
    }

    int getExactPathOrder() {
        return this.exactPath ? 0 : 1;
    }

}