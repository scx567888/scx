package cool.scx.util.http;

import cool.scx.util.http.HttpClientHelper;

import java.net.http.HttpRequest;

/**
 * a
 */
public final class EmptyBody implements HttpClientHelper.Body {

    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        return HttpRequest.BodyPublishers.noBody();
    }

}
