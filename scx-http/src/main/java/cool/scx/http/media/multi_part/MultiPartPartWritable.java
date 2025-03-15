package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.headers.content_type.ContentType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/// MultiPartPartWritable
///
/// @author scx567888
/// @version 0.0.1
public interface MultiPartPartWritable extends MultiPartPart {

    ScxHttpHeadersWritable headers();

    MultiPartPartWritable headers(ScxHttpHeaders headers);

    MultiPartPartWritable body(Supplier<InputStream> os);

    default MultiPartPartWritable contentType(ContentType contentType) {
        headers().contentType(contentType);
        return this;
    }

    default MultiPartPartWritable contentDisposition(ContentDisposition contentDisposition) {
        headers().contentDisposition(contentDisposition);
        return this;
    }

    default MultiPartPartWritable name(String name) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition != null) {
            contentDisposition.name(name);
            contentDisposition(contentDisposition);
        } else {
            contentDisposition(ContentDisposition.of().type("form-data").name(name));
        }
        return this;
    }

    default MultiPartPartWritable filename(String filename) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition != null) {
            contentDisposition.filename(filename);
            contentDisposition(contentDisposition);
        } else {
            contentDisposition(ContentDisposition.of().type("form-data").filename(filename));
        }
        return this;
    }

    default MultiPartPartWritable size(long size) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition != null) {
            contentDisposition.size(size);
            contentDisposition(contentDisposition);
        } else {
            contentDisposition(ContentDisposition.of().type("form-data").size(size));
        }
        return this;
    }

    default MultiPartPartWritable body(InputStream os) {
        return body(() -> os);
    }

    default MultiPartPartWritable body(byte[] os) {
        return body(() -> new ByteArrayInputStream(os));
    }

    default MultiPartPartWritable body(String os) {
        return body(() -> new ByteArrayInputStream(os.getBytes()));
    }

    default MultiPartPartWritable body(Path os) {
        return body(() -> {
            try {
                return Files.newInputStream(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
