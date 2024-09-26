package cool.scx.http.media;

import java.io.InputStream;

import static cool.scx.http.media.ByteArraySupport.BYTE_ARRAY_SUPPORT;
import static cool.scx.http.media.FormParamsSupport.FORM_PARAMS_SUPPORT;
import static cool.scx.http.media.InputStreamSupport.INPUT_STREAM_SUPPORT;
import static cool.scx.http.media.MultiPartSupport.MULTI_PART_SUPPORT;
import static cool.scx.http.media.StringSupport.STRING_SUPPORT;

public class MediaSupportSelector {

    @SuppressWarnings("unchecked")
    public static <T> MediaSupport<T> findMediaSupport(Class<T> type) {
        MediaSupport<?> mediaSupport = null;

        //优先查找已知类型
        if (type == byte[].class) {
            mediaSupport = BYTE_ARRAY_SUPPORT;
        } else if (type == String.class) {
            mediaSupport = STRING_SUPPORT;
        } else if (type == InputStream.class) {
            mediaSupport = INPUT_STREAM_SUPPORT;
        } else if (type == FormParams.class) {
            mediaSupport = FORM_PARAMS_SUPPORT;
        } else if (type == MultiPart.class) {
            mediaSupport = MULTI_PART_SUPPORT;
        }
        //todo 此处需要支持 spi 以便实现支持自定义类型

        if (mediaSupport == null) {
            throw new IllegalArgumentException("No MediaSupport found for " + type);
        }
        return (MediaSupport<T>) mediaSupport;
    }

}
