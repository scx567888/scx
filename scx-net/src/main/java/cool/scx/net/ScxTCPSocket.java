package cool.scx.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ScxTCPSocket {

    Socket socket();

    InputStream inputStream();

    OutputStream outputStream();

}
