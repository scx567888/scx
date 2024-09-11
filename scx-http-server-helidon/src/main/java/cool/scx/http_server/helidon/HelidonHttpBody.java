package cool.scx.http_server.helidon;

import cool.scx.http_server.ScxHttpBody;
import cool.scx.http_server.ScxHttpFormData;
import io.helidon.http.media.ReadableEntity;

import java.io.InputStream;

public class HelidonHttpBody implements ScxHttpBody {

    private final ReadableEntity content;

    public HelidonHttpBody(ReadableEntity content) {
        this.content = content;
    }

    @Override
    public InputStream asInputStream() {
        return content.hasEntity() ? content.as(InputStream.class) : null;
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
    public ScxHttpFormData asFormData() {
        //todo 这里会报错
        // return content.as(MultiPart.class);
        return content.as(ScxHttpFormData.class);
    }

}
