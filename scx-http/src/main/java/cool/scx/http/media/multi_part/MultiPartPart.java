package cool.scx.http.media.multi_part;

import cool.scx.http.body.BodyAlreadyConsumedException;
import cool.scx.http.body.BodyReadException;
import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.content_disposition.ContentDisposition;
import cool.scx.http.media.MediaReader;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.io.IOHelper;
import cool.scx.io.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import static cool.scx.http.routing.handler.StaticHelper.getMediaTypeByFile;

/// MultiPartPart
///
/// @author scx567888
/// @version 0.0.1
public interface MultiPartPart extends ScxHttpBody {

    static MultiPartPartWritable of() {
        return new MultiPartPartImpl();
    }

    static MultiPartPartWritable of(String name, String value) {
        return new MultiPartPartImpl().name(name).body(value);
    }

    static MultiPartPartWritable of(String name, byte[] value) {
        return new MultiPartPartImpl().name(name).body(value);
    }

    static MultiPartPartWritable of(String name, Path value) {
        var fileSize = IOHelper.getFileSize(value);
        var contentType = getMediaTypeByFile(value);
        var filename = value.getFileName().toString();
        return new MultiPartPartImpl().name(name).body(value).size(fileSize).filename(filename).contentType(contentType);
    }

    ScxHttpHeaders headers();

    Supplier<InputStream> body();

    @Override
    default InputStream inputStream() {
        return body().get();
    }

    @Override
    default <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException, BodyReadException {
        try {
            return t.read(inputStream(), headers());
        } catch (IOException e) {
            throw new BodyReadException(e);
        } catch (StreamClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

    default ScxMediaType contentType() {
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
