package cool.scx.http;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Copy From Vertx RouteImpl
public class PathMatcher {

    // Allow end users to select either the regular valid characters or the extender pattern
    private static final String RE_VAR_NAME = "[A-Za-z0-9_]+";

    // Pattern for :<token name> in path
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(" + RE_VAR_NAME + ")");

    // Pattern for (?<token name>) in path
    private static final Pattern RE_TOKEN_NAME_SEARCH = Pattern.compile("\\(\\?<(" + RE_VAR_NAME + ")>");

    // intersection of regex chars and https://tools.ietf.org/html/rfc3986#section-3.3
    private static final Pattern RE_OPERATORS_NO_STAR = Pattern.compile("([()$+.])");

    private String path;
    private Pattern pattern;
    private List<String> groups;
    private Set<String> namedGroupsInRegex;
    private boolean pathEndsWithSlash;
    private boolean exactPath;

    private PathMatcher() {
        this.path = null;
        this.pattern = null;
        this.groups = null;
        this.namedGroupsInRegex = null;
        this.pathEndsWithSlash = false;
        this.exactPath = true;
    }

    public static PathMatcher of(String path) {
        var pathMatcher = new PathMatcher();
        pathMatcher.checkPath(path);
        pathMatcher.setPath(path);
        return pathMatcher;
    }

    public static PathMatcher ofRegex(String regex) {
        var pathMatcher = new PathMatcher();
        pathMatcher.setRegex(regex);
        return pathMatcher;
    }

    private static boolean pathMatchesExact(String base, String other, boolean significantSlash) {
        // Ignore trailing slash when matching paths
        int len = other.length();

        if (significantSlash) {
            if (other.charAt(len -1) != '/') {
                // final slash is significant but missing
                return false;
            }
        } else {
            if (other.charAt(len -1) == '/') {
                // final slash is not significant, ignore it
                len--;
                if (base.length() != len) {
                    return false;
                }
                // content must match
                return other.regionMatches(0, base, 0, len);
            }
        }
        // content must match
        return other.equals(base);
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    private void checkPath(String path) {
        if ("".equals(path) || path.charAt(0) != '/') {
            throw new IllegalArgumentException("Path must start with /");
        }
    }

    private synchronized void setPath(String path) {
        // 查看路径是否为通配符 "*" 如果是，我们需要将此路径配置为不精确
        if (path.charAt(path.length() - 1) != '*') {
            this.exactPath = true;
            this.path = path;
        } else {
            this.exactPath = false;
            this.path = path.substring(0, path.length() - 1);
        }

        this.pathEndsWithSlash = this.path.endsWith("/");

        // 查看路径是否包含 ":" 如果是，则它包含参数捕获组，我们必须生成一个对应的正则表达式
        int params = 0;
        for (int i = 0; i < path.length(); i = i + 1) {
            if (path.charAt(i) == ':') {
                params = params + 1;
            }
        }
        if (params > 0) {
            int found = createPatternRegex(path);
            if (params != found) {
                throw new IllegalArgumentException("path param does not follow the variable naming rules, expected (" + params + ") found (" + found + ")");
            }
        }
    }

    private synchronized int createPatternRegex(String path) {
        // escape path from any regex special chars
        path = RE_OPERATORS_NO_STAR.matcher(path).replaceAll("\\\\$1");
        // allow usage of * at the end as per documentation
        if (path.charAt(path.length() - 1) == '*') {
            path = path.substring(0, path.length() - 1) + "(?<rest>.*)";
            this.exactPath = false;
        } else {
            this.exactPath = true;
        }

        // We need to search for any :<token name> tokens in the String and replace them with named capture groups
        Matcher m = RE_TOKEN_SEARCH.matcher(path);
        StringBuffer sb = new StringBuffer();
        List<String> groups = new ArrayList<>();
        int index = 0;
        while (m.find()) {
            String param = "p" + index;
            String group = m.group().substring(1);
            if (groups.contains(group)) {
                throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
            }
            m.appendReplacement(sb, "(?<" + param + ">[^/]+)");
            groups.add(group);
            index = index + 1;
        }
        m.appendTail(sb);
        if (this.exactPath && !this.pathEndsWithSlash) {
            sb.append("/?");
        }
        path = sb.toString();

        this.groups = groups;
        this.pattern = Pattern.compile(path);
        return index;
    }

    private synchronized void setRegex(String regex) {
        this.pattern = Pattern.compile(regex);
        this.exactPath = true;
        findNamedGroups(this.pattern.pattern());
    }

    private synchronized void findNamedGroups(String path) {
        Matcher m = RE_TOKEN_NAME_SEARCH.matcher(path);
        while (m.find()) {
            this.addNamedGroupInRegex(m.group(1));
        }
    }

    private void addNamedGroupInRegex(String namedGroupInRegex) {
        if (this.namedGroupsInRegex == null) {
            this.namedGroupsInRegex = new HashSet<>();
        }
        this.namedGroupsInRegex.add(namedGroupInRegex);
    }

    private boolean pathMatches(String requestPath, ParametersWritable pathParams) {
        final String thePath = path;
        final boolean pathEndsWithSlash = this.pathEndsWithSlash;

        // can be null
        if (requestPath == null) {
            requestPath = "/";
        }

        if (exactPath) {
            // exact path has no "rest"
            pathParams
                    .remove("*");

            return pathMatchesExact(thePath, requestPath, pathEndsWithSlash);
        } else {
            if (pathEndsWithSlash) {
                // the route expects a path that ends in "/*". This is a special case
                // we need to optionally allow any request just like if it was a "*" but
                // treat the slash
                final int pathLen = thePath.length();
                final int reqLen = requestPath.length();

                if (reqLen < pathLen - 2) {
                    // we miss at least 2 characters
                    return false;
                }

                if (reqLen == pathLen - 1) {
                    // request misses 1 character, there is the chance that this request doesn't include the final slash
                    // because the mount path ended with a wildcard we are relaxed in the check
                    if (thePath.regionMatches(0, requestPath, 0, pathLen - 1)) {
                        // handle the "rest" as path param *, always known to be empty
                        pathParams
                                .add("*", "/");
                        return true;
                    }
                }
            }

            if (requestPath.startsWith(thePath)) {
                // handle the "rest" as path param *
                pathParams
                        .add("*", requestPath.substring(thePath.length()));
                return true;
            }
            return false;
        }
    }

    private boolean matches(String requestPath, ParametersWritable pathParams) {

        if (path != null && pattern == null && !pathMatches(requestPath, pathParams)) {
            return false;
        }

        if (pattern == null) {
            return true;
        }

        // need to reset "rest"
        pathParams
                .remove("*");

        Matcher m;
        if (requestPath != null && (m = pattern.matcher(requestPath)).matches()) {

            var matchRest = -1;

            if (m.groupCount() <= 0) {
                return true;
            }
            if (!exactPath) {
                matchRest = m.start("rest");
                // always replace
                pathParams
                        .add("*", path.substring(matchRest));
            }

            if (!isEmpty(groups)) {
                // Pattern - named params
                // decode the path as it could contain escaped chars.
                final int len = Math.min(groups.size(), m.groupCount());
                for (int i = 0; i < len; i = i + 1) {
                    final String k = groups.get(i);
                    String undecodedValue;
                    // We try to take value in three ways:
                    // 1. group name of type p0, p1, pN (most frequent and used by vertx params)
                    // 2. group name inside the regex
                    // 3. No group name
                    try {
                        undecodedValue = m.group("p" + i);
                    } catch (IllegalArgumentException e) {
                        try {
                            undecodedValue = m.group(k);
                        } catch (IllegalArgumentException e1) {
                            // Groups starts from 1 (0 group is total match)
                            undecodedValue = m.group(i + 1);
                        }
                    }
                    if (undecodedValue != null) {
                        pathParams.add(k, undecodedValue);
                    }
                }
            } else {
                // Straight regex - un-named params
                // decode the path as it could contain escaped chars.
                if (!isEmpty(namedGroupsInRegex)) {
                    for (String namedGroup : namedGroupsInRegex) {
                        String namedGroupValue = m.group(namedGroup);
                        if (namedGroupValue != null) {
                            pathParams.add(namedGroup, namedGroupValue);
                        }
                    }
                }
                for (int i = 0; i < m.groupCount(); i = i + 1) {
                    String group = m.group(i + 1);
                    if (group != null) {
                        final String k = "param" + i;
                        pathParams.add(k, group);
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public MatchResult matches(String path) {
        var p = new ParametersImpl();
        var a = matches(path, p);
        return new MatchResult(a, p);
    }

    public record MatchResult(boolean accepted, Parameters params) {

    }

}
