package cool.scx.util.http;

import java.net.http.HttpRequest;

/**
 * a
 */
public interface Body {

    /**
     * a
     *
     * @param builder a
     * @return a
     */
    HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder);

}