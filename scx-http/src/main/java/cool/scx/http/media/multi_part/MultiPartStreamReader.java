package cool.scx.http.media.multi_part;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.http.media_type.MediaType.MULTIPART_FORM_DATA;

/// MultiPartStreamReader
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartStreamReader implements MediaReader<MultiPart> {

    public static final MultiPartStreamReader MULTI_PART_READER = new MultiPartStreamReader();

    private MultiPartStreamReader() {
        
    }

    @Override
    public MultiPart read(InputStream inputStream, ScxHttpHeaders headers) {
        var contentType = headers.contentType();
        // 分块传输依赖 contentType 所以这里需要强制校验
        if (contentType == null) {
            // 这里 不抛出客户端异常 因为 这是用户调用的, 问题不能怪罪到客户端
            throw new IllegalArgumentException("No Content-Type header found");
        }
        if (!MULTIPART_FORM_DATA.equalsIgnoreParams(contentType)) {
            // 同上 这里 不抛出客户端异常 因为 这是用户调用的, 问题不能怪罪到客户端
            throw new IllegalArgumentException("Content-Type is not multipart/form-data");
        }
        var boundary = contentType.boundary();
        if (boundary == null) {
            // 当 Content-Type 已经是 MULTIPART_FORM_DATA 了 ,  boundary 是必须的 所以这里抛出客户端错误 
            throw new BadRequestException("No boundary found");
        }
        return new MultiPartStream(inputStream, boundary);
    }

}
