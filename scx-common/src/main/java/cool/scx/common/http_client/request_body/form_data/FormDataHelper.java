package cool.scx.common.http_client.request_body.form_data;

import cool.scx.common.http_client.request_body.FormData;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class FormDataHelper {

    /**
     * 创建编码器
     *
     * @param formData FormData
     * @param request  a {@link HttpRequest} object
     * @return a {@link HttpPostRequestEncoder} object
     * @throws HttpPostRequestEncoder.ErrorDataEncoderException if any.
     * @throws IOException                                      if any.
     */
    public static HttpPostRequestEncoder initEncoder(FormData formData, HttpRequest request) throws HttpPostRequestEncoder.ErrorDataEncoderException, IOException {
        var encoder = new HttpPostRequestEncoder(new DefaultHttpDataFactory(), request, true, UTF_8, HttpPostRequestEncoder.EncoderMode.HTML5);
        for (var formDataPart : formData.items()) {
            switch (formDataPart.type()) {
                case ATTRIBUTE -> encoder.addBodyAttribute(
                        formDataPart.name(),
                        formDataPart.attributeValue()
                );
                case FILE_UPLOAD_PATH -> encoder.addBodyFileUpload(
                        formDataPart.name(),
                        formDataPart.filename(),
                        formDataPart.fileUploadPath().toFile(),
                        formDataPart.contentType(),
                        false
                );
                case FILE_UPLOAD_BYTES -> {
                    var fileUpload = new MemoryFileUpload(
                            formDataPart.name(),
                            formDataPart.filename(),
                            formDataPart.contentType(),
                            null,
                            null,
                            formDataPart.fileUploadPathBytes().length
                    );
                    fileUpload.setContent(Unpooled.buffer().writeBytes(formDataPart.fileUploadPathBytes()));
                    encoder.addBodyHttpData(fileUpload);
                }
            }
        }
        encoder.finalizeRequest();
        return encoder;
    }

}
