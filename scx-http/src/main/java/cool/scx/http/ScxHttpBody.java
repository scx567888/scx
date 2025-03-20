package cool.scx.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.media.MediaReader;
import cool.scx.http.media.event_stream.ClientEventStream;
import cool.scx.http.media.event_stream.ClientEventStreamReader;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartStreamCachedReader;
import cool.scx.http.media.object.ObjectReader;
import cool.scx.http.media.path.PathReader;
import cool.scx.http.media.string.StringReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.http.media.byte_array.ByteArrayReader.BYTE_ARRAY_READER;
import static cool.scx.http.media.event_stream.ClientEventStreamReader.CLIENT_EVENT_STREAM_READER;
import static cool.scx.http.media.form_params.FormParamsReader.FORM_PARAMS_READER;
import static cool.scx.http.media.json_node.JsonNodeReader.JSON_NODE_READER;
import static cool.scx.http.media.multi_part.MultiPartStreamCachedReader.MULTI_PART_READER_CACHED;
import static cool.scx.http.media.multi_part.MultiPartStreamReader.MULTI_PART_READER;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/// ScxHttpBody
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpBody {

    InputStream inputStream();

    <T> T as(MediaReader<T> t);

    default byte[] asBytes() {
        return as(BYTE_ARRAY_READER);
    }

    default String asString() {
        return as(STRING_READER);
    }

    default String asString(Charset charset) {
        return as(new StringReader(charset));
    }

    default FormParams asFormParams() {
        return as(FORM_PARAMS_READER);
    }

    default MultiPart asMultiPart() {
        return as(MULTI_PART_READER);
    }

    default MultiPart asMultiPartCached() {
        return as(MULTI_PART_READER_CACHED);
    }

    default MultiPart asMultiPartCached(Path cachePath) {
        return as(new MultiPartStreamCachedReader(cachePath));
    }

    default Path asPath(Path path, OpenOption... options) {
        return as(new PathReader(path, options));
    }

    default JsonNode asJsonNode() {
        return as(JSON_NODE_READER);
    }

    default <T> T asObject(Class<T> c) {
        return as(new ObjectReader<>(c));
    }

    default <T> T asObject(TypeReference<T> c) {
        return as(new ObjectReader<>(c));
    }

    default ClientEventStream asEventStream() {
        return as(CLIENT_EVENT_STREAM_READER);
    }

}
