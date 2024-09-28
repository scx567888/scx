package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_disposition.ContentDispositionWritable;
import cool.scx.http.content_type.ContentTypeWritable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public interface MultiPartPartWritable extends MultiPartPart {

    ScxHttpHeadersWritable headers();

    MultiPartPartWritable headers(ScxHttpHeadersWritable headers);

    MultiPartPartWritable body(Supplier<InputStream> os);

    default MultiPartPartWritable contentType(ContentTypeWritable contentType) {
        headers().contentType(contentType);
        return this;
    }

    default MultiPartPartWritable contentDisposition(ContentDispositionWritable contentDisposition) {
        headers().contentDisposition(contentDisposition);
        return this;
    }

    default MultiPartPartWritable name(String name) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition == null) {
            contentDisposition = ContentDisposition.of();
            contentDisposition(contentDisposition);
        }
        contentDisposition.name(name);
        return this;
    }

    default MultiPartPartWritable filename(String filename) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition == null) {
            contentDisposition = ContentDisposition.of();
            contentDisposition(contentDisposition);
        }
        contentDisposition.filename(filename);
        return this;
    }

    default MultiPartPartWritable size(long size) {
        var contentDisposition = headers().contentDisposition();
        if (contentDisposition == null) {
            contentDisposition = ContentDisposition.of();
            contentDisposition(contentDisposition);
        }
        contentDisposition.size(size);
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
