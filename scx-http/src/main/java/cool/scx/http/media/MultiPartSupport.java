package cool.scx.http.media;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerRequestHeaders;

import java.io.InputStream;

public class MultiPartSupport implements MediaSupport<MultiPart> {

    public static final MultiPartSupport MULTI_PART_SUPPORT = new MultiPartSupport();

    @Override
    public MultiPart read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
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
