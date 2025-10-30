package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.io.consumer.OutputStreamByteConsumer;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media_type.MediaType.MULTIPART_FORM_DATA;

/// MultiPartWriter
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartWriter implements MediaWriter {

    private final MultiPart multiPart;

    public MultiPartWriter(MultiPart multiPart) {
        this.multiPart = multiPart;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentType() == null) {
            // MULTIPART 有很多类型 这里暂时只当成 MULTIPART_FORM_DATA
            responseHeaders.contentType(ScxMediaType.of(MULTIPART_FORM_DATA).boundary(this.multiPart.boundary()));
        }
        // 正常来说我们是可以通过计算拿到 MultiPart 的长度, 但是这里我们为了简化计算量, 仍然采取未知
        return -1;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
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
                try (var i = multiPartPart.byteInput()) {
                    i.readAll(new OutputStreamByteConsumer(outputStream));
                }
                //写入换行符
                outputStream.write(l);
            }
            outputStream.write(f);
        }
    }

}
