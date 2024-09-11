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
        return content.as(InputStream.class);
    }

    @Override
    public byte[] asBytes() {
        return content.as(byte[].class);
    }

    @Override
    public String asString() {
        return content.as(String.class);
    }

    @Override
    public ScxHttpFormData asFormData() {
        //todo 这里会报错
        // return content.as(MultiPart.class);
        return content.as(ScxHttpFormData.class);
    }

}
