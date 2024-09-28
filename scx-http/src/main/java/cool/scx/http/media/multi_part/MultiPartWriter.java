package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

import static cool.scx.http.MediaType.MULTIPART_FORM_DATA;

public class MultiPartWriter implements MediaWriter {

    private final MultiPart multiPart;

    public MultiPartWriter(MultiPart multiPart) {
        this.multiPart = multiPart;
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        if (headersWritable.contentType() == null) {
            headersWritable.contentType(ContentType.of().mediaType(MULTIPART_FORM_DATA).boundary(this.multiPart.boundary()));
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        //发送头
        //发送每个内容
        for (var multiPartPart : multiPart) {
            var body = multiPartPart.body();
            try (var i = body.get()) {
                i.transferTo(outputStream);
            } catch (Exception e) {

            }
        }
    }

}
