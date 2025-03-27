package cool.scx.http.media.form_params;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;

import static cool.scx.http.media.form_params.FormParamsHelper.decodeFormParams;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/// FormParamsReader
///
/// @author scx567888
/// @version 0.0.1
public class FormParamsReader implements MediaReader<FormParams> {

    public static final FormParamsReader FORM_PARAMS_READER = new FormParamsReader();

    private FormParamsReader() {

    }

    @Override
    public FormParams read(InputStream inputStream, ScxHttpHeaders headers) throws IOException {
        // FormParams 本质上就是字符串 所以这里使用 STRING_READER 先进行内容读取
        var str = STRING_READER.read(inputStream, headers);
        return decodeFormParams(str);
    }

}
