package cool.scx.http;

import cool.scx.common.util.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.impl.URIDecoder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathMatcher {

    private boolean exactPath;
    private boolean pathEndsWithSlash;
    private List<String> groups;
    private Pattern pattern;
    private String path;
    private Set<String> namedGroupsInRegex;

    private PathMatcher() {

    }

    public static PathMatcher ofPath(String path) {
        var pathMatcher = new PathMatcher();
        pathMatcher.checkPath(path);
        pathMatcher.setPath(path);
        return pathMatcher;
    }

    public static PathMatcher ofPathRegex(String regex) {
        var pathMatcher = new PathMatcher();
        pathMatcher.setRegex(regex);
        return pathMatcher;
    }


    private void checkPath(String path) {
        if ("".equals(path) || path.charAt(0) != '/') {
            throw new IllegalArgumentException("Path must start with /");
        }
    }

    private synchronized void setPath(String path) {
        // See if the path is a wildcard "*" is present - If so we need to configure this path to be not exact
        if (path.charAt(path.length() - 1) != '*') {
            this.setExactPath(true);
            this.setPath0(path);
        } else {
            this.setExactPath(false);
            this.setPath0(path.substring(0, path.length() - 1));
        }

        this.setPathEndsWithSlash(this.getPath().endsWith("/"));

        // See if the path contains ":" - if so then it contains parameter capture groups and we have to generate
        // a regex for that
        int params = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == ':') {
                params++;
            }
        }
        if (params > 0) {
            int found = createPatternRegex(path);
            if (params != found) {
                throw new IllegalArgumentException("path param does not follow the variable naming rules, expected (" + params + ") found (" + found + ")");
            }
        }
    }

    // Allow end users to select either the regular valid characters or the extender pattern
    private static final String RE_VAR_NAME = Boolean.getBoolean("io.vertx.web.route.param.extended-pattern") ?
            "[A-Za-z_$][A-Za-z0-9_$-]*" :
            "[A-Za-z0-9_]+";

    // Pattern for :<token name> in path
    private static final Pattern RE_TOKEN_SEARCH = Pattern.compile(":(" + RE_VAR_NAME + ")");
    // Pattern for (?<token name>) in path
    private static final Pattern RE_TOKEN_NAME_SEARCH = Pattern.compile("\\(\\?<(" + RE_VAR_NAME + ")>");

    // intersection of regex chars and https://tools.ietf.org/html/rfc3986#section-3.3
    private static final Pattern RE_OPERATORS_NO_STAR = Pattern.compile("([\\(\\)\\$\\+\\.])");

    private synchronized int createPatternRegex(String path) {
        // escape path from any regex special chars
        path = RE_OPERATORS_NO_STAR.matcher(path).replaceAll("\\\\$1");
        // allow usage of * at the end as per documentation
        if (path.charAt(path.length() - 1) == '*') {
            path = path.substring(0, path.length() - 1) + "(?<rest>.*)";
            this.setExactPath(false);
        } else {
            this.setExactPath(true);
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
            index++;
        }
        m.appendTail(sb);
        if (this.isExactPath() && !this.isPathEndsWithSlash()) {
            sb.append("/?");
        }
        path = sb.toString();

        this.setGroups(groups);
        this.setPattern(Pattern.compile(path));
        return index;
    }

    private synchronized void setRegex(String regex) {
        this.setPattern(Pattern.compile(regex));
        this.setExactPath(true);
        findNamedGroups(this.getPattern().pattern());
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

    public boolean isPathEndsWithSlash() {
        return pathEndsWithSlash;
    }

    public void setPathEndsWithSlash(boolean pathEndsWithSlash) {
        this.pathEndsWithSlash = pathEndsWithSlash;
    }

    public void setPath0(String path0) {
        this.path = path0;
    }

    public String getPath() {
        return path;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setExactPath(boolean exactPath) {
        this.exactPath = exactPath;
    }

    public boolean isExactPath() {
        return exactPath;
    }



    private boolean pathMatches(String requestPath,MultiMap<String,String> pathParams) {
        final String thePath = path;
        final boolean pathEndsWithSlash = this.pathEndsWithSlash;

        // can be null
        if (requestPath == null) {
            requestPath = "/";
        }

        if (exactPath) {
            // exact path has no "rest"
            pathParams
                    .removeAll("*");

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
                                .put("*", "/");
                        return true;
                    }
                }
            }

            if (requestPath.startsWith(thePath)) {
                // handle the "rest" as path param *
                pathParams
                        .put("*", URIDecoder.decodeURIComponent(requestPath.substring(thePath.length()), false));
                return true;
            }
            return false;
        }
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

    public int matches(String requestPath,  MultiMap<String,String> pathParams) {
        
        if (path != null && pattern == null && !pathMatches(requestPath, pathParams)) {
            return 404;
        }
        if (pattern != null) {
            // need to reset "rest"
            pathParams
                    .removeAll("*");

            String path = requestPath;

            Matcher m;
            if (path != null && (m = pattern.matcher(path)).matches()) {

                var matchRest = -1;

                if (m.groupCount() > 0) {
                    if (!exactPath) {
                        matchRest = m.start("rest");
                        // always replace
                        pathParams
                                .put("*", path.substring( matchRest));
                    }

                    if (!isEmpty(groups)) {
                        // Pattern - named params
                        // decode the path as it could contain escaped chars.
                        final int len = Math.min(groups.size(), m.groupCount());
                        for (int i = 0; i < len; i++) {
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
                                pathParams.put(k, undecodedValue);
                            }
                        }
                    } else {
                        // Straight regex - un-named params
                        // decode the path as it could contain escaped chars.
                        if (!isEmpty(namedGroupsInRegex)) {
                            for (String namedGroup : namedGroupsInRegex) {
                                String namedGroupValue = m.group(namedGroup);
                                if (namedGroupValue != null) {
                                    pathParams.put(namedGroup, namedGroupValue);
                                }
                            }
                        }
                        for (int i = 0; i < m.groupCount(); i++) {
                            String group = m.group(i + 1);
                            if (group != null) {
                                final String k = "param" + i;
                                pathParams.put(k, group);
                            }
                        }
                    }
                }
            } else {
                return 404;
            }
        }
        
        return 0;
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    //todo 
    public MatchResult match(URIPath uriPath) {
        MultiMap<String,String> s=new MultiMap<>();
        var m=matches(uriPath.path(),s);
        return new MatchResult(m==0,s);
    }

    public record MatchResult(boolean accepted, MultiMap<String, String> params) {

    }

    public static void main(String[] args) {
        PathMatcher s = PathMatcher.ofPath("/asdcasdcasdc/:id/ccc");
        var sss=new HashMap<String, String>();
        var b = s.match(new URIPath() {
            @Override
            public String path() {
                return "/asdcasdcasdc/v/ccc";
            }

            @Override
            public URIQuery query() {
                return null;
            }
        });
        System.out.println();
    }

}
