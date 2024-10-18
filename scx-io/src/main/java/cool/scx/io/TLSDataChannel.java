package cool.scx.io;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

//todo 待完成
public class TLSDataChannel extends BaseDataChannel<ByteChannel> {

    private final SSLEngine sslEngine;

    public TLSDataChannel(ByteChannel channel, SSLEngine sslEngine) {
        super(channel);
        this.sslEngine = sslEngine;
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        // 在写入前进行加密
        ByteBuffer encryptedBuffer = ByteBuffer.allocate(buffer.remaining() + sslEngine.getSession().getPacketBufferSize());
        sslEngine.wrap(buffer, encryptedBuffer);
        encryptedBuffer.flip();
        super.write(encryptedBuffer);
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        // 在读取后进行解密
        ByteBuffer encryptedBuffer = ByteBuffer.allocate(buffer.remaining() + sslEngine.getSession().getPacketBufferSize());
        int bytesRead = super.read(encryptedBuffer);
        encryptedBuffer.flip();
        sslEngine.unwrap(encryptedBuffer, buffer);
        return buffer.position();
    }
    
}
