package cool.scx.http.body;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.media.MediaReader;
import cool.scx.http.media.event_stream.ClientEventStream;
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

    <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException, BodyReadException;

    default byte[] asBytes() throws BodyReadException, BodyAlreadyConsumedException {
        return as(BYTE_ARRAY_READER);
    }

    default String asString() throws BodyReadException, BodyAlreadyConsumedException {
        return as(STRING_READER);
    }

    default String asString(Charset charset) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new StringReader(charset));
    }

    default FormParams asFormParams() throws BodyReadException, BodyAlreadyConsumedException {
        return as(FORM_PARAMS_READER);
    }

    default MultiPart asMultiPart() throws BodyReadException, BodyAlreadyConsumedException {
        return as(MULTI_PART_READER);
    }

    default MultiPart asMultiPartCached() throws BodyReadException, BodyAlreadyConsumedException {
        return as(MULTI_PART_READER_CACHED);
    }

    default MultiPart asMultiPartCached(Path cachePath) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new MultiPartStreamCachedReader(cachePath));
    }

    default Path asPath(Path path, OpenOption... options) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new PathReader(path, options));
    }

    default JsonNode asJsonNode() throws BodyReadException, BodyAlreadyConsumedException {
        return as(JSON_NODE_READER);
    }

    default <T> T asObject(Class<T> c) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new ObjectReader<>(c));
    }

    default <T> T asObject(TypeReference<T> c) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new ObjectReader<>(c));
    }

    default ClientEventStream asEventStream() throws BodyReadException, BodyAlreadyConsumedException {
        return as(CLIENT_EVENT_STREAM_READER);
    }

    default GzipBody asGzipBody() throws BodyReadException, BodyAlreadyConsumedException {
        return as(GzipBody::new);
    }

    default CacheBody asCacheBody() throws BodyReadException, BodyAlreadyConsumedException {
        return as(CacheBody::new);
    }

}
