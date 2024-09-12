package cool.scx.http;

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

    //todo 
    public MatchResult match(URIPath uriPath) {
        return null;
    }

    public record MatchResult(boolean accepted, Map<String, String> params) {

    }

    public static void main(String[] args) {
        PathMatcher s = PathMatcher.ofPathRegex("/asdcasdcasdc/*");
        System.out.println();
    }

}
