package cool.scx.web;

import cool.scx.common.standard.HttpMethod;
import cool.scx.common.util.ObjectUtils;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.impl.RouteImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 用于承载数据
 */
record RouteState(Map<String, Object> metadata, String path, String name, int order, boolean enabled,
                  Set<HttpMethod> methods, Set<MIMEHeader> consumes, boolean emptyBodyPermittedWithConsumes,
                  Set<MIMEHeader> produces, boolean added, Pattern pattern, List<String> groups,
                  boolean useNormalizedPath, Set<String> namedGroupsInRegex, Pattern virtualHostPattern,
                  boolean pathEndsWithSlash, boolean exclusive, boolean exactPath) {

    private static final Method VERTX_ROUTE_STATE_METHOD = initVertxRouteStateMethod();

    /**
     * 此处因为 vertx 包访问权限端原因 我们无法直接获取 RouteState 这里通过反射进行特殊处理 然后使用一个 类似的数据结构进行包装
     *
     * @param r t
     * @return t
     */
    public static RouteState getRouteState(Route r) {
        try {
            return ObjectUtils.convertValue(VERTX_ROUTE_STATE_METHOD.invoke(r), RouteState.class);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method initVertxRouteStateMethod() {
        try {
            var m = RouteImpl.class.getDeclaredMethod("state");
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    int getGroupsOrder() {
        return this.groups == null ? 0 : this.groups.size();
    }

    int getExactPathOrder() {
        return this.exactPath ? 0 : 1;
    }

}
