package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_type.ContentType;

import java.io.InputStream;
import java.util.function.Supplier;

public interface MultiPartPart {

    static MultiPartPartWritable of() {
        return new MultiPartPartImpl();
    }

    ScxHttpHeaders headers();

    Supplier<InputStream> body();

    default ContentType contentType() {
        return headers().contentType();
    }

    default ContentDisposition contentDisposition() {
        return headers().contentDisposition();
    }

    default String name() {
        var contentDisposition = contentDisposition();
        return contentDisposition != null ? contentDisposition.name() : null;
    }

    default String filename() {
        var contentDisposition = contentDisposition();
        return contentDisposition != null ? contentDisposition.filename() : null;
    }

    default Long size() {
        var contentDisposition = contentDisposition();
        return contentDisposition != null ? contentDisposition.size() : null;
    }

}
