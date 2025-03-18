package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.media_type.ScxMediaType;

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

    default MultiPartPartWritable contentType(ScxMediaType contentType) {
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
            contentDisposition(ContentDisposition.of(contentDisposition).name(name));
        } else {
            contentDisposition(ContentDisposition.of().type("form-data").name(name));
        }
        return this;
    }

    default MultiPartPartWritable filename(String filename) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition != null) {
            contentDisposition(ContentDisposition.of(contentDisposition).filename(filename));
        } else {
            contentDisposition(ContentDisposition.of().type("form-data").filename(filename));
        }
        return this;
    }

    default MultiPartPartWritable size(long size) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition != null) {
            contentDisposition(ContentDisposition.of(contentDisposition).size(size));
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
