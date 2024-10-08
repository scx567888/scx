package cool.scx.http.routing;

import cool.scx.http.Parameters;

/**
 * PathMatcher
 */
public interface PathMatcher {

    static PathMatcher any() {
        return path -> new MatchResult(true, Parameters.of());
    }

    static PathMatcher of(String path) {
        var pathMatcher = new PathMatcherImpl();
        pathMatcher.checkPath(path);
        pathMatcher.setPath(path);
        return pathMatcher;
    }

    static PathMatcherImpl ofRegex(String regex) {
        var pathMatcher = new PathMatcherImpl();
        pathMatcher.setRegex(regex);
        return pathMatcher;
    }

    PathMatcher.MatchResult matches(String path);

    record MatchResult(boolean accepted, Parameters<String, String> pathParams) {

    }

}
