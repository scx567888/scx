package cool.scx.util.http;

import java.net.http.HttpRequest;

/**
 * a
 */
final class EmptyBody implements Body {

    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        return HttpRequest.BodyPublishers.noBody();
    }

}
