package cool.scx.http.content_disposition;

import cool.scx.http.ParametersWritable;

public interface ContentDispositionWritable extends ContentDisposition {

    ContentDispositionWritable type(String type);

    ContentDispositionWritable params(ParametersWritable<String, String> params);

}
