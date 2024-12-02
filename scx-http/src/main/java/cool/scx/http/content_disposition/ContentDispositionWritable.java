package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

/**
 * ContentDispositionWritable
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ContentDispositionWritable extends ContentDisposition {

    ParametersWritable<String, String> params();

    ContentDispositionWritable type(String type);

    ContentDispositionWritable params(Parameters<String, String> params);

    default ContentDispositionWritable name(String name) {
        params().set("name", name);
        return this;
    }

    default ContentDispositionWritable filename(String filename) {
        params().set("filename", filename);
        return this;
    }

    default ContentDispositionWritable size(long size) {
        params().set("size", size + "");
        return this;
    }

}
