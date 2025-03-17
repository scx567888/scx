package cool.scx.http.media.multi_part;

import cool.scx.http.media_type.MediaType;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

/// MultiPartStreamReader
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartStreamReader implements MediaReader<MultiPart> {

    public static final MultiPartStreamReader MULTI_PART_READER = new MultiPartStreamReader();

    @Override
    public MultiPart read(InputStream inputStream, ScxHttpHeaders headers) {
        var contentType = headers.contentType();
        if (contentType == null) {
            throw new IllegalArgumentException("No Content-Type header found");
        }
        if (contentType.mediaType() != MediaType.MULTIPART_FORM_DATA) {
            throw new IllegalArgumentException("Content-Type is not multipart/form-data");
        }
        var boundary = contentType.boundary();
        if (boundary == null) {
            throw new IllegalArgumentException("No boundary found");
        }
        return new MultiPartStream(inputStream, boundary);
    }

}
