package cool.scx.http;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartStreamCachedReader;
import cool.scx.http.media.path.PathReader;
import cool.scx.http.media.string.StringReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.http.media.byte_array.ByteArrayReader.BYTE_ARRAY_READER;
import static cool.scx.http.media.form_params.FormParamsReader.FORM_PARAMS_READER;
import static cool.scx.http.media.multi_part.MultiPartStreamCachedReader.CACHED_MULTI_PART_READER;
import static cool.scx.http.media.multi_part.MultiPartStreamReader.MULTI_PART_READER;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/**
 * ScxHttpBody
 */
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

    default MultiPart asCachedMultiPart() {
        return as(CACHED_MULTI_PART_READER);
    }

    default MultiPart asCachedMultiPart(Path cachePath) {
        return as(new MultiPartStreamCachedReader(cachePath));
    }

    default Path asPath(Path path, OpenOption... options) {
        return as(new PathReader(path, options));
    }

}
