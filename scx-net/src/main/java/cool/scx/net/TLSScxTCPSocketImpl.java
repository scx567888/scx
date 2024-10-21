package cool.scx.net;

import cool.scx.io.ByteChannelDataChannel;
import cool.scx.io.NoMoreDataException;
import cool.scx.io.TLSDataChannel;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class TLSScxTCPSocketImpl  implements ScxTCPSocket {

    private final ByteChannelDataChannel<SocketChannel> rawChannel;
    private final SSLEngine sslEngine;
    private final SocketChannel socketChannel;
    private final TLSDataChannel tlsChannel;

    public TLSScxTCPSocketImpl(SocketChannel socketChannel, SSLEngine sslEngine) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;
        this.rawChannel = new ByteChannelDataChannel<>(socketChannel);
        this.tlsChannel = new TLSDataChannel(this.rawChannel,this.sslEngine);
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        this.tlsChannel.write(buffer);
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        this.tlsChannel.write(bytes,offset,length);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        this.tlsChannel.write(bytes);
    }

    @Override
    public void write(Path path, long offset, long length) throws IOException {
        this.tlsChannel.write(path,offset,length);
    }

    @Override
    public void write(Path path) throws IOException {
        this.tlsChannel.write(path);
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return this.tlsChannel.read(buffer);
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return this.tlsChannel.read(bytes,offset,length);
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return this.tlsChannel.read(bytes);
    }

    @Override
    public void read(Path path, long offset, long length, OpenOption... options) throws IOException {
        this.tlsChannel.read(path,offset,length,options);
    }

    @Override
    public void read(Path path, OpenOption... options) throws IOException {
        this.tlsChannel.read(path,options);
    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        return this.tlsChannel.read(maxLength);
    }

    @Override
    public void close() throws IOException {
        this.tlsChannel.close();
    }

    @Override
    public boolean isOpen() throws IOException {
        return this.tlsChannel.isOpen();
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return null;
    }

}
