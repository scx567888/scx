package cool.scx.http.media.object;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;

import java.io.OutputStream;

public class ObjectWriter implements MediaWriter {

    private final Object object;
    private final JsonNode jsonNode;
    private final JsonNodeWriter jsonNodeWriter;

    public ObjectWriter(Object object) {
        this.object = object;
        this.jsonNode = ObjectUtils.jsonMapper().valueToTree(object);
        this.jsonNodeWriter = new JsonNodeWriter(this.jsonNode);
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        jsonNodeWriter.beforeWrite(headersWritable, headers);
    }

    @Override
    public void write(OutputStream outputStream) {
        jsonNodeWriter.write(outputStream);
    }

}
