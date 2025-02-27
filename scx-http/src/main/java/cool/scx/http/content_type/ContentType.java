package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ScxMediaType;

import java.nio.charset.Charset;

import static cool.scx.http.content_type.ContentTypeHelper.decodedContentType;

/// ContentType
///
/// @author scx567888
/// @version 0.0.1
public interface ContentType {

    static ContentTypeWritable of() {
        return new ContentTypeImpl();
    }

    static ContentTypeWritable of(String contentTypeStr) {
        return decodedContentType(contentTypeStr);
    }

    static ContentTypeWritable of(ScxMediaType mediaType) {
        return new ContentTypeImpl().mediaType(mediaType);
    }

    static ContentTypeWritable of(ContentType oldContentType) {
        return new ContentTypeImpl(oldContentType);
    }

    ScxMediaType mediaType();

    Parameters<String, String> params();

    default Charset charset() {
        var charset = params().get("charset");
        return charset == null ? null : Charset.forName(charset);
    }

    default String boundary() {
        return params().get("boundary");
    }

    String encode();

}
