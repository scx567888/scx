package cool.scx.http.media.form_params;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media_type.MediaType.APPLICATION_X_WWW_FORM_URLENCODED;
import static java.nio.charset.StandardCharsets.UTF_8;

/// FormParamsWriter
///
/// @author scx567888
/// @version 0.0.1
public class FormParamsWriter implements MediaWriter {

    private final FormParams formParams;
    private byte[] bytes;

    public FormParamsWriter(FormParams formParams) {
        this.formParams = formParams;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(APPLICATION_X_WWW_FORM_URLENCODED);
        }
        bytes = formParams.encode().getBytes(UTF_8);
        return bytes.length;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        try (outputStream) {
            outputStream.write(bytes);
        }
    }

}
