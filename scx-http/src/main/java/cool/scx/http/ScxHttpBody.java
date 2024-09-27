package cool.scx.http;

import cool.scx.http.media.form_params.FormParams;
import cool.scx.http.media.MediaReader;
import cool.scx.http.media.multi_part.MultiPart;

import java.io.InputStream;

import static cool.scx.http.media.byte_array.ByteArrayReader.BYTE_ARRAY_READER;
import static cool.scx.http.media.form_params.FormParamsReader.FORM_PARAMS_READER;
import static cool.scx.http.media.multi_part.MultiPartReader.MULTI_PART_READER;
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

    default FormParams asFormParams() {
        return as(FORM_PARAMS_READER);
    }

    default MultiPart asMultiPart() {
        return as(MULTI_PART_READER);
    }

}
