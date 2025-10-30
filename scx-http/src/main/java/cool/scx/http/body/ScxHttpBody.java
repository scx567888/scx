package cool.scx.http.body;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.event_stream.ClientEventStream;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.object.ObjectReader;
import cool.scx.http.media.path.PathReader;
import cool.scx.http.media.string.StringReader;
import cool.scx.io.ByteInput;
import cool.scx.object.node.Node;
import cool.scx.reflect.TypeReference;

import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.http.media.byte_array.ByteArrayReader.BYTE_ARRAY_READER;
import static cool.scx.http.media.event_stream.ClientEventStreamReader.CLIENT_EVENT_STREAM_READER;
import static cool.scx.http.media.form_params.FormParamsReader.FORM_PARAMS_READER;
import static cool.scx.http.media.multi_part.MultiPartStreamReader.MULTI_PART_READER;
import static cool.scx.http.media.string.StringReader.STRING_READER;
import static cool.scx.http.media.tree.TreeReader.TREE_READER;

/// ScxHttpBody
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpBody {

    ByteInput byteInput();

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

    default Path asPath(Path path, OpenOption... options) throws BodyReadException, BodyAlreadyConsumedException {
        return as(new PathReader(path, options));
    }

    default FormParams asFormParams() throws BodyReadException, BodyAlreadyConsumedException {
        return as(FORM_PARAMS_READER);
    }

    default MultiPart asMultiPart() throws BodyReadException, BodyAlreadyConsumedException {
        return as(MULTI_PART_READER);
    }

    default Node asTree() throws BodyReadException, BodyAlreadyConsumedException {
        return as(TREE_READER);
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
