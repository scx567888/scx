package cool.scx.http.x.http1;

import cool.scx.io.OutputStreamByteOutput;

import java.io.IOException;
import java.io.OutputStream;

/// NoCloseOutputStream
///
/// @author scx567888
/// @version 0.0.1
public class NoCloseOutputStream extends OutputStreamByteOutput {

    public NoCloseOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() {
        //这里我们不去关闭流
        this.flush();
    }

}
