package cool.scx.http.media.form_params;

import cool.scx.http.ScxHttpServerRequestHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static cool.scx.http.media.string.StringReader.STRING_READER;

public class FormParamsReader implements MediaReader<FormParams> {

    public static final FormParamsReader FORM_PARAMS_READER = new FormParamsReader();

    private FormParamsReader() {

    }

    @Override
    public FormParams read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        var formParams = new FormParams();
        var str = STRING_READER.read(inputStream, headers);
        var pairs = str.split("&");
        for (var pair : pairs) {
            var keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                formParams.add(key, value);
            }
        }
        return formParams;
    }

}
