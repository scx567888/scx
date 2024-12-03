package cool.scx.http.usagi;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NoCloseOutputStream extends FilterOutputStream {

    public NoCloseOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() {
        //这里什么也不做
    }

}
