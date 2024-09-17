package cool.scx.http.helidon;

import cool.scx.http.ScxHttpBody;
import io.helidon.http.media.ReadableEntity;

import java.io.InputStream;

/**
 * HelidonHttpBody
 */
class HelidonHttpBody implements ScxHttpBody {

    private final ReadableEntity content;

    public HelidonHttpBody(ReadableEntity content) {
        this.content = content;
    }

    @Override
    public InputStream inputStream() {
        return content.inputStream();
    }

    @Override
    public byte[] asBytes() {
        return content.hasEntity() ? content.as(byte[].class) : null;
    }

    @Override
    public String asString() {
        return content.hasEntity() ? content.as(String.class) : null;
    }

    @Override
    public <T> T as(Class<T> t) {
        return content.hasEntity() ? content.as(t) : null;
    }

}
