package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;

import static cool.scx.http.content_disposition.ContentDispositionHelper.decodedContentDisposition;

public interface ContentDisposition {

    static ContentDispositionWritable of() {
        return new ContentDispositionImpl();
    }

    static ContentDispositionWritable of(String contentDispositionStr) {
        return decodedContentDisposition(contentDispositionStr);
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

    default String size() {
        return params().get("size");
    }

}
