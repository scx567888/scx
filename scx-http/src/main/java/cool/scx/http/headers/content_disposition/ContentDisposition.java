package cool.scx.http.headers.content_disposition;

import cool.scx.http.parameters.Parameters;

import static cool.scx.http.headers.content_disposition.ContentDispositionHelper.decodedContentDisposition;

/// ContentDisposition
///
/// @author scx567888
/// @version 0.0.1
public interface ContentDisposition {

    static ContentDispositionWritable of() {
        return new ContentDispositionImpl();
    }

    static ContentDispositionWritable of(String contentDispositionStr) {
        return decodedContentDisposition(contentDispositionStr);
    }

    static ContentDispositionWritable of(ContentDisposition oldContentDisposition) {
        return new ContentDispositionImpl(oldContentDisposition);
    }

    String type();

    Parameters<String, String> params();

    default String name() {
        return params().get("name");
    }

    default String filename() {
        return params().get("filename");
    }

    default String creationDate() {
        return params().get("creation-date");
    }

    default String modificationDate() {
        return params().get("modification-date");
    }

    default String readDate() {
        return params().get("read-date");
    }

    default Long size() {
        var size = params().get("size");
        return size != null ? Long.valueOf(size) : null;
    }

    String encode();

}
