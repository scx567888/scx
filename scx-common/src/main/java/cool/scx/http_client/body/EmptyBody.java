package cool.scx.http_client.body;

import cool.scx.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest;

/**
 * <p>EmptyBody class.</p>
 *
 * @author scx567888
 * @version 2.0.5
 */
public final class EmptyBody implements ScxHttpClientRequestBody {

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpRequest.BodyPublisher bodyPublisher(HttpRequest.Builder builder) {
        return HttpRequest.BodyPublishers.noBody();
    }

}
