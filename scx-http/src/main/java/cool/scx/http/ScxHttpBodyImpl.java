package cool.scx.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ScxHttpBodyImpl
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ScxHttpBodyImpl implements ScxHttpBody {

    private final AtomicBoolean alreadyRead;
    private final InputStream inputStream;
    private final ScxHttpHeaders headers;

    /**
     * @param inputStream   inputStream
     * @param headers       headers
     * @param maxBufferSize 最大缓冲数量 如果小于 0 则不缓冲
     */
    public ScxHttpBodyImpl(InputStream inputStream, ScxHttpHeaders headers, int maxBufferSize) {
        if (maxBufferSize > 0) {
            this.inputStream = new BufferedInputStream(inputStream, maxBufferSize);
            this.inputStream.mark(0);
        } else {
            this.inputStream = inputStream;
        }
        this.headers = headers;
        this.alreadyRead = new AtomicBoolean(false);
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public InputStream inputStream() {
        if (inputStream instanceof BufferedInputStream) {
            try {
                inputStream.reset();
                return inputStream;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (alreadyRead.get()) {
                throw new IllegalStateException("非缓冲模式的 Body 只能读取一次 !!!");
            }
            alreadyRead.set(true);
            return inputStream;
        }
    }

    @Override
    public String toString() {
        return asString();
    }

}
