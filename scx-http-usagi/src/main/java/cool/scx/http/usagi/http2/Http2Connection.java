package cool.scx.http.usagi.http2;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.usagi.UsagiHttpServerOptions;
import cool.scx.io.BufferedInputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Consumer;

import static cool.scx.http.usagi.http2.Http2Helper.HTTP2_CONNECTION_PREFACE;

//todo 未完成
public class Http2Connection {

    private final static System.Logger LOGGER = System.getLogger(Http2Connection.class.getName());

    private final ScxTCPSocket tcpSocket;
    private final UsagiHttpServerOptions options;
    private final Consumer<ScxHttpServerRequest> requestHandler;
    private final LinkedDataReader dataReader;
    private final OutputStream dataWriter;
    private State state;

    public Http2Connection(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
        this.tcpSocket = tcpSocket;
        this.options = options;
        this.requestHandler = requestHandler;
        this.dataReader = new LinkedDataReader(new BufferedInputStreamDataSupplier(this.tcpSocket.inputStream()));
        this.dataWriter = this.tcpSocket.outputStream();
    }

    public void start() {
        try {
            readPreface();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("新连接");
        while (true) {
            try {
                // 读取帧头
                Http2FrameHeader frame = readFrameHeader();

                // 根据帧类型处理
                handleFrameHeader(frame);

                System.out.println(frame.toString());

            } catch (IOException e) {
                LOGGER.log(System.Logger.Level.ERROR, "处理连接时发生错误", e);
                break;
            }
        }
        // 循环结束则关闭连接
        try {
            tcpSocket.close();
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.TRACE, "关闭 Socket 时发生错误！", e);
        }
    }

    private void readPreface() throws IOException {
        var preface = dataReader.read(HTTP2_CONNECTION_PREFACE.length);
        if (Arrays.compare(preface, HTTP2_CONNECTION_PREFACE) != 0) {
            throw new IllegalStateException("Invalid HTTP/2 connection preface: \n");
        }
    }

    private Http2FrameHeader readFrameHeader() throws IOException {
        //固定 9 个字节
        byte[] header = dataReader.read(9);

        int length = ((header[0] & 0xFF) << 16) | ((header[1] & 0xFF) << 8) | (header[2] & 0xFF);
        var type = Http2FrameType.of((byte) (header[3] & 0xFF));
        byte flags = (byte) (header[4] & 0xFF);
        int streamId = ((header[5] & 0x7F) << 24) | ((header[6] & 0xFF) << 16) | ((header[7] & 0xFF) << 8) | (header[8] & 0xFF);

        return new Http2FrameHeader(length, type, flags, streamId);
    }

    private void handleFrameHeader(Http2FrameHeader frameHeader) throws IOException {
        switch (frameHeader.type()) {
            case DATA -> doData();
            case HEADERS -> doHeaders();
            case PRIORITY -> doPriority();
            case RST_STREAM -> doRstStream();
            case SETTINGS -> doSettings();
            case PUSH_PROMISE -> doPushPromise();
            case PING -> doPing();
            case GOAWAY -> doGoaway();
            case WINDOW_UPDATE -> doWindowUpdate();
            case CONTINUATION -> doContinuation();
            default -> doUnknown();
        }
    }


    private void doData() {

    }

    private void doUnknown() {

    }

    private void doContinuation() {

    }

    private void doWindowUpdate() {

    }

    private void doGoaway() {

    }

    private void doPing() {

    }

    private void doPushPromise() {

    }

    private void doSettings() {

    }

    private void doRstStream() {

    }

    private void doPriority() {

    }

    private void doHeaders() {

    }

}
