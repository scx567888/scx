package cool.scx.http;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.event_stream.ServerEventStream;
import cool.scx.http.media.event_stream.ServerEventStreamWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.gzip.GzipSender;
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

public interface ScxHttpSender<T> {

    T send(MediaWriter writer);

    //******************** send 操作 *******************

    default T send() {
        return send(EMPTY_WRITER);
    }

    default T send(byte[] bytes) {
        return send(new ByteArrayWriter(bytes));
    }

    default T send(String str) {
        return send(new StringWriter(str));
    }

    default T send(String str, Charset charset) {
        return send(new StringWriter(str, charset));
    }

    default T send(Path path) {
        return send(new PathWriter(path));
    }

    default T send(Path path, long offset, long length) {
        return send(new PathWriter(path, offset, length));
    }

    default T send(InputStream inputStream) {
        return send(new InputStreamWriter(inputStream));
    }

    default T send(FormParams formParams) {
        return send(new FormParamsWriter(formParams));
    }

    default T send(MultiPart multiPart) {
        return send(new MultiPartWriter(multiPart));
    }

    default T send(JsonNode jsonNode) {
        return send(new JsonNodeWriter(jsonNode));
    }

    default T send(Object object) {
        return send(new ObjectWriter(object));
    }

    default ScxHttpSender<T> sendGzip() {
        //禁止多次包装
        if (this instanceof GzipSender<T>) {
            return this;
        }
        return new GzipSender<T>(this);
    }

    //理论上只有 服务器才支持发送这种格式
    default ServerEventStream sendEventStream() {
        var writer = new ServerEventStreamWriter();
        send(writer);
        return writer.eventStream();
    }

}
