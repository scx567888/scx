package cool.scx.http_server.impl.helidon;

import cool.scx.http_server.Helper;
import cool.scx.http_server.ScxHttpBody;
import io.helidon.common.parameters.Parameters;
import io.helidon.http.media.ReadableEntity;
import io.helidon.http.media.multipart.MultiPart;

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
    public MultiPart asMultiPart() {
        return content.as(MultiPart.class);
    }

    @Override
    public Parameters asFormData() {
        return content.as(Helper.FormDataBuilder.class);
    }

}
