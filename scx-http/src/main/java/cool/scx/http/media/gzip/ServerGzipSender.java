package cool.scx.http.media.gzip;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.event_stream.ServerEventStream;
import cool.scx.http.media.event_stream.ServerEventStreamWriter;
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

public class ServerGzipSender {

    private final ScxHttpServerResponse serverResponse;

    public ServerGzipSender(ScxHttpServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    public void send(MediaWriter writer) {
        this.serverResponse.send(new GzipWriter(writer));
    }

    public void send() {
        send(EMPTY_WRITER);
    }

    public void send(byte[] bytes) {
        send(new ByteArrayWriter(bytes));
    }

    public void send(String str) {
        send(new StringWriter(str));
    }

    public void send(String str, Charset charset) {
        send(new StringWriter(str, charset));
    }

    public void send(Path path) {
        send(new PathWriter(path));
    }

    public void send(Path path, long offset, long length) {
        send(new PathWriter(path, offset, length));
    }

    public void send(InputStream inputStream) {
        send(new InputStreamWriter(inputStream));
    }

    public void send(FormParams formParams) {
        send(new FormParamsWriter(formParams));
    }

    public void send(MultiPart multiPart) {
        send(new MultiPartWriter(multiPart));
    }

    public void send(JsonNode jsonNode) {
        send(new JsonNodeWriter(jsonNode));
    }

    public void send(Object object) {
        send(new ObjectWriter(object));
    }

    public ServerEventStream sendEventStream() {
        var writer = new ServerEventStreamWriter();
        send(writer);
        return writer.eventStream();
    }

}
