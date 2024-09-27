package cool.scx.http.media.multi_part;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

public class MultiPartReader implements MediaReader<MultiPart> {

    public static final MultiPartReader MULTI_PART_READER = new MultiPartReader();

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
        return new MultiPart(inputStream, boundary);
    }

}
