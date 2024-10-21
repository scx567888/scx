package cool.scx.io;

import cool.scx.common.functional.ScxConsumer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class TLSCodec {

    private final SSLEngine sslEngine;

    public TLSCodec(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }

    private static ByteBuffer enlargeBuffer(ByteBuffer buffer) {
        ByteBuffer largerBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
        buffer.flip();
        largerBuffer.put(buffer);
        return largerBuffer;
    }


    private static ByteBuffer ensureCapacity(ByteBuffer buffer, int additionalCapacity) {
        if (buffer.remaining() >= additionalCapacity) {
            return buffer;
        }
        ByteBuffer largerBuffer = ByteBuffer.allocate(buffer.capacity() * 2 + additionalCapacity);
        buffer.flip();
        largerBuffer.put(buffer);
        return largerBuffer;
    }

    public ByteBuffer encode(ByteBuffer buffer) throws IOException {
        var packetBufferSize = sslEngine.getSession().getPacketBufferSize();
        //存储加密后的数据
        var encryptedData = ByteBuffer.allocate(buffer.remaining() + packetBufferSize);

        //循环加密
        while (buffer.hasRemaining()) {
            // 临时加密数据
            var tempEncryptedData = ByteBuffer.allocate(packetBufferSize);
            // 加密
            var result = sslEngine.wrap(buffer, tempEncryptedData);

            var status = result.getStatus();
            //判断是否需要扩容等
            switch (status) {
                case OK -> {
                    // 切换到读模式 
                    tempEncryptedData.flip();
                    encryptedData.put(tempEncryptedData);
                }
                case BUFFER_OVERFLOW -> {
                    // 扩展缓冲区并重试
                    encryptedData = enlargeBuffer(encryptedData);
                }
                case BUFFER_UNDERFLOW -> throw new IOException("SSLEngine wrap failed with status: " + status);
                case CLOSED -> throw new IOException("SSLEngine wrap failed with status: " + status);
            }
        }

        // 切换到读模式
        encryptedData.flip();
        return encryptedData;
    }

    public void encode(ByteBuffer buffer, ScxConsumer<ByteBuffer, IOException> consumer) throws IOException {
        int packetBufferSize = sslEngine.getSession().getPacketBufferSize();

        while (buffer.hasRemaining()) {
            var tempEncryptedData = ByteBuffer.allocate(packetBufferSize);
            var result = sslEngine.wrap(buffer, tempEncryptedData);
            var status = result.getStatus();

            switch (status) {
                case OK -> {
                    // 切换到读模式并传递给消费者
                    tempEncryptedData.flip();
                    consumer.accept(tempEncryptedData);
                }
                case BUFFER_OVERFLOW -> throw new IOException("SSLEngine wrap encountered BUFFER_OVERFLOW");
                case BUFFER_UNDERFLOW -> throw new IOException("SSLEngine wrap encountered BUFFER_UNDERFLOW");
                case CLOSED -> throw new IOException("SSLEngine wrap encountered CLOSED");
            }
        }

    }

    public byte[] encode(byte[] bytes) throws IOException {
        // 将字节数组包装成 ByteBuffer
        var buffer = ByteBuffer.wrap(bytes);
        
        // 调用已有的 encode(ByteBuffer buffer) 方法
        var encryptedData = encode(buffer);

        // 将加密后的 ByteBuffer 转换为字节数组并返回
        byte[] encryptedBytes = new byte[encryptedData.remaining()];
        encryptedData.get(encryptedBytes);
        return encryptedBytes;
    }

    public void encode(byte[] bytes, ScxConsumer<byte[], IOException> consumer) throws IOException {
        // 将字节数组包装成 ByteBuffer
        var buffer = ByteBuffer.wrap(bytes);
        
        // 调用已有的 encode(ByteBuffer buffer) 方法
        encode(buffer, (encryptedData) -> {
            // 将加密后的 ByteBuffer 转换为字节数组并返回
            byte[] encryptedBytes = new byte[encryptedData.remaining()];
            encryptedData.get(encryptedBytes);
            consumer.accept(encryptedBytes);
        });
    }

    public ByteBuffer decode(ByteBuffer encryptedBuffer) throws IOException {
        int appBufferSize = sslEngine.getSession().getApplicationBufferSize();

        //存储解密后的数据
        var decryptedData = ByteBuffer.allocate(appBufferSize);

        //循环解密
        while (encryptedBuffer.hasRemaining()) {
            //临时解密数据
            var tempDecryptedData = ByteBuffer.allocate(appBufferSize);
            //解密
            var result = sslEngine.unwrap(encryptedBuffer, tempDecryptedData);

            var status = result.getStatus();

            //判断是否需要扩容等
            switch (status) {
                case OK:
                    tempDecryptedData.flip();
                    decryptedData = ensureCapacity(decryptedData, tempDecryptedData.remaining());
                    decryptedData.put(tempDecryptedData);
                    break;
                case BUFFER_OVERFLOW:
                    // 扩展应用数据缓冲区的大小
                    decryptedData = enlargeBuffer(decryptedData);
                    break;
                case BUFFER_UNDERFLOW:
                    // 缓冲区数据不足，继续读取更多数据
                    // 在这里添加读取更多数据的逻辑，例如从 SocketChannel 读取更多数据
                    // 对于解密流程，这部分通常由上层逻辑处理
                    throw new IOException("SSLEngine unwrap needs more data");
                case CLOSED:
                    throw new IOException("SSLEngine has closed");
            }
        }

        //切换到读模式
        decryptedData.flip();
        return decryptedData;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        SSLEngine sslEngine1 = SSLContext.getDefault().createSSLEngine();
        sslEngine1.setUseClientMode(true);
        var s = new TLSCodec(sslEngine1);
        var sb = new StringBuilder();
        for (int i = 0; i < 999; i++) {
            sb.append(i + ",");
        }
        sb.append("end");
        var encode = s.encode(sb.toString().getBytes());
//        ByteBuffer decode = s.decode(encode);
        System.out.println();
    }

}
