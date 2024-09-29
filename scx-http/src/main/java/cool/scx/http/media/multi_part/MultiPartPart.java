package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.media.path.PathHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import static cool.scx.http.routing.handler.StaticHelper.getMediaTypeByFile;

public interface MultiPartPart {

    static MultiPartPartWritable of() {
        return new MultiPartPartImpl();
    }

    static MultiPartPartWritable of(String name, String value) {
        return new MultiPartPartImpl().name(name).body(value);
    }

    static MultiPartPartWritable of(String name, Path value) {
        var fileSize = PathHelper.getFileSize(value);
        var contentType = getMediaTypeByFile(value);
        return new MultiPartPartImpl().name(name).body(value).size(fileSize).contentType(contentType);
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

    default byte[] bodyBytes() {
        try (var b = body().get()) {
            return b.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
