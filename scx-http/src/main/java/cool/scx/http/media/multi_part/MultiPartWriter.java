package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

import static cool.scx.http.MediaType.MULTIPART_FORM_DATA;

/**
 * MultiPartWriter
 *
 * @author scx567888
 * @version 0.0.1
 */
public class MultiPartWriter implements MediaWriter {

    private final MultiPart multiPart;

    public MultiPartWriter(MultiPart multiPart) {
        this.multiPart = multiPart;
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentType() == null) {
            // MULTIPART 有很多类型 这里暂时只当成 MULTIPART_FORM_DATA
            responseHeaders.contentType(ContentType.of().mediaType(MULTIPART_FORM_DATA).boundary(this.multiPart.boundary()));
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        //头
        var h = ("--" + multiPart.boundary() + "\r\n").getBytes();
        //尾
        var f = ("--" + multiPart.boundary() + "--\r\n").getBytes();
        //换行符
        var l = "\r\n".getBytes();
        try (outputStream) {
            //发送每个内容
            for (var multiPartPart : multiPart) {
                //发送头
                outputStream.write(h);
                var headers = multiPartPart.headers().encode();
                //写入头
                outputStream.write(headers.getBytes());
                //写入换行符
                outputStream.write(l);
                //写入内容
                try (var i = multiPartPart.inputStream()) {
                    i.transferTo(outputStream);
                }
                //写入换行符
                outputStream.write(l);
            }
            outputStream.write(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
