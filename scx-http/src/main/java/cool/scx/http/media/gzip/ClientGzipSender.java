package cool.scx.http.media.gzip;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.json_node.JsonNodeWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;

public class ClientGzipSender {

    private final ScxHttpClientRequest clientRequest;

    public ClientGzipSender(ScxHttpClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public ScxHttpClientResponse send(MediaWriter writer) {
        return this.clientRequest.send(new GzipWriter(writer));
    }

    public ScxHttpClientResponse send() {
        return send(EMPTY_WRITER);
    }

    public ScxHttpClientResponse send(byte[] bytes) {
        return send(new ByteArrayWriter(bytes));
    }

    public ScxHttpClientResponse send(String str) {
        return send(new StringWriter(str));
    }

    public ScxHttpClientResponse send(String str, Charset charset) {
        return send(new StringWriter(str, charset));
    }

    public ScxHttpClientResponse send(Path path) {
        return send(new PathWriter(path));
    }

    public ScxHttpClientResponse send(Path path, long offset, long length) {
        return send(new PathWriter(path, offset, length));
    }

    public ScxHttpClientResponse send(InputStream inputStream) {
        return send(new InputStreamWriter(inputStream));
    }

    public ScxHttpClientResponse send(FormParams formParams) {
        return send(new FormParamsWriter(formParams));
    }

    public ScxHttpClientResponse send(MultiPart multiPart) {
        return send(new MultiPartWriter(multiPart));
    }

    public ScxHttpClientResponse send(JsonNode jsonNode) {
        return send(new JsonNodeWriter(jsonNode));
    }

    public ScxHttpClientResponse send(Object object) {
        return send(new ObjectWriter(object));
    }

}
