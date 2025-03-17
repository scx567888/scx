package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;

import java.nio.charset.Charset;

/// ScxMediaType
///
/// @author scx567888
/// @version 0.0.1
public interface ScxMediaType {

    static ScxMediaTypeWritable of() {
        return new ScxMediaTypeImpl();
    }

    static ScxMediaTypeWritable of(ScxMediaType oldMediaType) {
        return new ScxMediaTypeImpl(oldMediaType.type(), oldMediaType.subtype(), oldMediaType.params());
    }

    static ScxMediaTypeWritable of(String mediaTypeStr) {
        return ScxMediaTypeHelper.decodedMediaType(mediaTypeStr);
    }

    String type();

    String subtype();

    Parameters<String, String> params();

    default boolean equalsIgnoreParams(ScxMediaType other) {
        if (other == null) {
            return false;
        }
        return type().equalsIgnoreCase(other.type()) && subtype().equalsIgnoreCase(other.subtype());
    }

    default String encode() {
        return ScxMediaTypeHelper.encodeMediaType(this);
    }

    default Charset charset() {
        var charset = params().get("charset");
        return charset == null ? null : Charset.forName(charset);
    }

    default String boundary() {
        return params().get("boundary");
    }

}
