package cool.scx.tcp.tls_channel;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static cool.scx.io.IOHelper.transferByteBuffer;
import static cool.scx.tcp.tls_channel.UnwrapResult.Status.*;

/// TLSSocketChannel
///
/// @author scx567888
/// @version 0.0.1
public class TLSSocketChannel extends AbstractSocketChannel {

    private final SSLEngine sslEngine;

    // 存储应用数据 (已加密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    // 这里不是 final 的是因为本身可能会进行扩容
    private ByteBuffer outboundNetData;

    // 存储网络数据 (未解密) 同时会缓存 tcp 半包 
    // 这里不是 final 的是因为本身需要用来存储 半包数据
    private ByteBuffer inboundNetData;

    // 存储应用数据 (已解密) 这里不使用其缓存任何数据 仅仅是为了减少频繁创建 ByteBuffer 造成的性能损失
    // 这里不是 final 的是因为本身可能会进行扩容
    private ByteBuffer inboundAppData;

    public TLSSocketChannel(SocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
        //这里默认容量采用 SSLSession 提供的 以便减小扩容的概率
        var session = sslEngine.getSession();
        var packetBufferSize = session.getPacketBufferSize();
        var applicationBufferSize = session.getApplicationBufferSize();
        this.outboundNetData = ByteBuffer.allocate(packetBufferSize); //永远都是写模式
        this.inboundNetData = ByteBuffer.allocate(packetBufferSize); //永远都是写模式
        this.inboundAppData = ByteBuffer.allocate(applicationBufferSize).flip(); //永远都是读模式
    }

    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();

        _MAIN:
        while (true) {
            var handshakeStatus = sslEngine.getHandshakeStatus();
            switch (handshakeStatus) {
                case NEED_UNWRAP -> {
                    //握手阶段我们不需要强制读取 因为 inboundNetData 中可能存在半包
                    //我们需要将 inboundNetData 用尽才去读取
                    var result = unwrap(false);
                    switch (result.status) {
                        case SOCKET_CHANNEL_CLOSED ->
                                throw new SSLHandshakeException("Channel closed during handshake");
                        case CLOSED -> throw new SSLHandshakeException("closed on handshake wrap");
                    }
                }
                case NEED_WRAP -> {
                    // 加密一个空的数据 用于握手
                    var result = wrap(ByteBuffer.allocate(0));
                    switch (result.status) {
                        case OK -> {
                        }
                        case BUFFER_OVERFLOW -> throw new SSLHandshakeException("BUFFER_OVERFLOW on handshake wrap");
                        case BUFFER_UNDERFLOW -> throw new SSLHandshakeException("BUFFER_UNDERFLOW on handshake wrap");
                        case CLOSED -> throw new SSLHandshakeException("CLOSED on handshake wrap");
                    }
                }
                case NEED_TASK -> {
                    // 处理委派任务 这里我们直接在当前线程中运行
                    while (true) {
                        var task = sslEngine.getDelegatedTask();
                        if (task == null) {
                            break;
                        }
                        task.run();
                    }
                }
                case FINISHED -> {
                    // 握手完成 退出主循环
                    break _MAIN;
                }
                case NOT_HANDSHAKING -> {
                    // 当前不在进行握手操作，退出主循环
                    break _MAIN;
                }
            }
        }
    }

    //调用前 inboundAppData 是读模式
    //调用后 inboundAppData 仍然是读模式
    @Override
    public int read(ByteBuffer dst) throws IOException {
        //0, 如果有剩余则先使用剩余的
        if (inboundAppData.hasRemaining()) {
            return transferByteBuffer(inboundAppData, dst);
        }

        //1, 重置缓冲区以进行新的读取操作
        inboundAppData.clear();

        //这里强制读取 因为实际测试中 强制读取会减少 unwrap 的半包错误尝试次数
        var result = unwrap(true);

        //切换到读模式
        inboundAppData.flip();

        switch (result.status) {
            case SOCKET_CHANNEL_CLOSED -> {
                return -1;
            }
            //如果是关闭帧 则执行关闭
            case CLOSED -> sslEngine.closeInbound();
        }

        // 写入到用户 buffer 中
        return transferByteBuffer(inboundAppData, dst);
    }

    //调用前请保证 outboundNetData 是写入模式
    //调用后 outboundNetData 仍然是写入模式
    @Override
    public int write(ByteBuffer src) throws IOException {
        var result = wrap(src);

        switch (result.status) {
            case BUFFER_OVERFLOW -> throw new SSLException("BUFFER_OVERFLOW on handshake wrap");
            case BUFFER_UNDERFLOW -> throw new SSLException("BUFFER_UNDERFLOW on handshake wrap");
            case CLOSED -> throw new SSLException("CLOSED on handshake wrap");
        }

        return result.bytesConsumed;
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        //1, 关闭出站
        sslEngine.closeOutbound();
        while (!sslEngine.isOutboundDone()) {
            wrap(ByteBuffer.allocate(0));
        }

        //2, 重置缓冲区以进行新的读取操作
        inboundAppData.clear();

        //2, 读取剩余数据
        unwrap(true);

        //4,关闭底层连接
        socketChannel.close();
    }

    //调用前请保证 outboundNetData 是写入模式
    //调用后 outboundNetData 是读模式
    public WrapResult wrap(ByteBuffer src) throws IOException {
        var wrapResult = new WrapResult();

        _MAIN:
        while (true) {
            //重置缓冲区为写入模式
            outboundNetData.clear();

            //执行 wrap 加密
            var result = sslEngine.wrap(src, outboundNetData);

            //更新 字节消费与生产数量
            wrapResult.bytesConsumed += result.bytesConsumed();
            wrapResult.bytesProduced += result.bytesProduced();

            //设置 状态
            wrapResult.status = result.getStatus();

            switch (result.getStatus()) {
                case OK -> {
                    //切换到读模式
                    outboundNetData.flip();

                    //循环发送
                    while (outboundNetData.hasRemaining()) {
                        socketChannel.write(outboundNetData);
                    }

                    //只有原始数据全部用尽我们才跳出循环
                    if (!src.hasRemaining()) {
                        break _MAIN;
                    }
                }
                case BUFFER_OVERFLOW -> {
                    // 直接扩容即可 以 2 倍为基准 (这种情况很少发生)
                    outboundNetData = ByteBuffer.allocate(outboundNetData.capacity() * 2);
                }
                case BUFFER_UNDERFLOW -> {
                    break _MAIN;
                }
                case CLOSED -> {
                    // 即使遇到 CLOSED 我们也需要向远端写入 关闭数据帧
                    // 切换到读模式
                    outboundNetData.flip();

                    //循环发送
                    while (outboundNetData.hasRemaining()) {
                        socketChannel.write(outboundNetData);
                    }

                    break _MAIN;
                }
            }
        }

        return wrapResult;
    }

    //调用前请保证 inboundNetData 是写入模式 , inboundAppData 是写入模式
    //调用后会尝试从网络通道中读取数据并解密 inboundNetData 仍然是写入模式 , inboundAppData 仍然是写入模式
    public UnwrapResult unwrap(boolean forceRead) throws IOException {
        //计算本次 get 总的 解密和加密数据量, 用于判断当遇见 TCP 半包时是直接终止还是继续 read 
        var unwrapResult = new UnwrapResult();

        //如果不是强制读 我们根据 inboundNetData 中是否存在剩余数据来判断是否需要读取
        var needRead = forceRead || inboundNetData.position() == 0;

        //这里涉及到如果遇到 tcp 半包 我们需要尝试重新读取 (如果第一次就遇到了) 所以这里采用一个 while 循环
        _MAIN:
        while (true) {

            if (needRead) {
                //读取远程数据到入站网络缓冲区
                var bytesRead = socketChannel.read(inboundNetData);
                if (bytesRead == -1) {
                    unwrapResult.status = SOCKET_CHANNEL_CLOSED;
                    return unwrapResult;
                }
            }

            //转换为读模式 准备用于解密
            inboundNetData.flip();

            //这里我们尝试解密所有读取到的数据 直到剩余解密数据为空或者遇到 TCP 半包
            while (inboundNetData.hasRemaining()) {

                var result = sslEngine.unwrap(inboundNetData, inboundAppData);

                //更新 字节消费与生产数量
                unwrapResult.bytesConsumed += result.bytesConsumed();
                unwrapResult.bytesProduced += result.bytesProduced();

                switch (result.getStatus()) {
                    case OK -> {
                        unwrapResult.status = OK;
                        // 解密成功, 但这里有时会出现无法消耗任何数据的情况 为了防止死循环这里跳出
                        if (result.bytesProduced() == 0 && result.bytesConsumed() == 0) {
                            break _MAIN;
                        }
                    }
                    case BUFFER_OVERFLOW -> {
                        unwrapResult.status = BUFFER_OVERFLOW;
                        // appBuffer 容量不足, 这里进行扩容 2 倍
                        // 原有的已经解密的数据别忘了放进去
                        var newAppBuffer = ByteBuffer.allocate(inboundAppData.capacity() * 2);
                        newAppBuffer.put(inboundAppData.flip()); // 这里 appBuffer 是写状态 所以需要翻转一下
                        inboundAppData = newAppBuffer;
                    }
                    case BUFFER_UNDERFLOW -> {
                        unwrapResult.status = BUFFER_UNDERFLOW;
                        // 这里表示 netBuffer 中待解密数据不足 这里分为三种情况
                        // 1, 如果已经成功解密了部分数据 我们跳过这次的扩容
                        if (unwrapResult.bytesProduced > 0) {
                            break _MAIN;
                        }
                        //计算 inboundNetData 是不是满了
                        var isFull = inboundNetData.limit() == inboundNetData.capacity();
                        // 2, 如果满了 (没有可写入的空间)，则扩容
                        if (isFull) {
                            var newInboundNetData = ByteBuffer.allocate(inboundNetData.capacity() * 2);
                            // 原有的已经读取的数据别忘了放进去
                            newInboundNetData.put(inboundNetData);// 这里 netBuffer 已经是读状态 不需要 flip()
                            inboundNetData = newInboundNetData;
                        } else {
                            //3, 有剩余表示 只是上一次读取的少了 可以直接使用 inboundNetData.compact(); 但是因为会进行数组的复制
                            // 这里为了性能 手动调整缓冲区指针 然后重新读取
                            inboundNetData.position(inboundNetData.limit());
                            // 设置位置到剩余数据的末尾 
                            inboundNetData.limit(inboundNetData.capacity());
                        }
                        // 这里我们需要继续读取
                        needRead = true;
                        continue _MAIN;
                    }
                    case CLOSED -> {
                        unwrapResult.status = CLOSED;
                        //通道关闭 但是我们有可能之前已经读取到了部分数据这里需要 跳出循环以便返回剩余数据
                        break _MAIN;
                    }
                }
            }

            //退出主循环
            break;
        }

        //这里我们的 netBuffer 中可能有一些残留数据 我们压缩一下 以便下次继续使用
        inboundNetData.compact();

        return unwrapResult;
    }

    public SSLEngine sslEngine() {
        return sslEngine;
    }

}
