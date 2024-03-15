package cool.scx.common.http_client.body;

import cool.scx.common.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static java.net.http.HttpRequest.BodyPublishers.noBody;

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
    public BodyPublisher bodyPublisher(Builder builder) {
        return noBody();
    }

}
