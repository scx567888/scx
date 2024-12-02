package cool.scx.http.media.form_params;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.http.media.form_params.FormParamsHelper.decodeFormParams;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/**
 * FormParamsReader
 *
 * @author scx567888
 * @version 0.0.1
 */
public class FormParamsReader implements MediaReader<FormParams> {

    public static final FormParamsReader FORM_PARAMS_READER = new FormParamsReader();

    private FormParamsReader() {

    }

    @Override
    public FormParams read(InputStream inputStream, ScxHttpHeaders headers) {
        var str = STRING_READER.read(inputStream, headers);
        return decodeFormParams(str);
    }

}
