package cool.scx.http.media.form_params;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media.form_params.FormParamsHelper.encodeFormParams;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * FormParamsWriter
 *
 * @author scx567888
 * @version 0.0.1
 */
public class FormParamsWriter implements MediaWriter {

    private final FormParams formParams;
    private final byte[] bytes;

    public FormParamsWriter(FormParams formParams) {
        this.formParams = formParams;
        this.bytes = encodeFormParams(formParams).getBytes(UTF_8);
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentLength() == null) {
            responseHeaders.contentLength(bytes.length);
        }
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(ContentType.of(MediaType.APPLICATION_X_WWW_FORM_URLENCODED));
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
