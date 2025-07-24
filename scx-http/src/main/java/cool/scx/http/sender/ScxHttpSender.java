package cool.scx.http.sender;

import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.byte_array.ByteArrayWriter;
import cool.scx.http.media.event_stream.ServerEventStream;
import cool.scx.http.media.event_stream.ServerEventStreamWriter;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.form_params.FormParamsWriter;
import cool.scx.http.media.input_stream.InputStreamWriter;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media.object.ObjectWriter;
import cool.scx.http.media.path.PathWriter;
import cool.scx.http.media.string.StringWriter;
import cool.scx.http.media.tree.TreeWriter;
import cool.scx.object.node.Node;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static cool.scx.http.media.empty.EmptyWriter.EMPTY_WRITER;

public interface ScxHttpSender<T> {

    T send(MediaWriter writer) throws BodyAlreadySentException, HttpSendException;

    //******************** send 操作 *******************

    default T send() throws HttpSendException, BodyAlreadySentException {
        return send(EMPTY_WRITER);
    }

    default T send(InputStream inputStream) throws BodyAlreadySentException, HttpSendException {
        return send(new InputStreamWriter(inputStream));
    }

    default T send(byte[] bytes) throws BodyAlreadySentException, HttpSendException {
        return send(new ByteArrayWriter(bytes));
    }

    default T send(String str) throws BodyAlreadySentException, HttpSendException {
        return send(new StringWriter(str));
    }

    default T send(String str, Charset charset) throws BodyAlreadySentException, HttpSendException {
        return send(new StringWriter(str, charset));
    }

    default T send(Path path) throws BodyAlreadySentException, HttpSendException {
        return send(new PathWriter(path));
    }

    default T send(Path path, long offset, long length) throws BodyAlreadySentException, HttpSendException {
        return send(new PathWriter(path, offset, length));
    }

    default T send(FormParams formParams) throws BodyAlreadySentException, HttpSendException {
        return send(new FormParamsWriter(formParams));
    }

    default T send(MultiPart multiPart) throws BodyAlreadySentException, HttpSendException {
        return send(new MultiPartWriter(multiPart));
    }

    default T send(Node node) throws BodyAlreadySentException, HttpSendException {
        return send(new TreeWriter(node));
    }

    default T send(Object object) throws BodyAlreadySentException, HttpSendException {
        return send(new ObjectWriter(object));
    }

    //理论上只有 服务器才支持发送这种格式
    default ServerEventStream sendEventStream() throws BodyAlreadySentException, HttpSendException {
        var writer = new ServerEventStreamWriter();
        send(writer);
        return writer.eventStream();
    }

    default ScxHttpSender<T> sendGzip() {
        return new GzipSender<>(this);
    }

}
