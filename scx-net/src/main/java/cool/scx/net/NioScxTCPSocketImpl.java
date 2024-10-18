package cool.scx.net;

import cool.scx.io.ByteChannelDataChannel;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class NioScxTCPSocketImpl extends ByteChannelDataChannel<SocketChannel> implements ScxTCPSocket {

    public NioScxTCPSocketImpl(SocketChannel socketChannel) {
        super(socketChannel);
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return channel.getRemoteAddress();
    }

}
