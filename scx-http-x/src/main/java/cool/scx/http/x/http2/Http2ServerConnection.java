package cool.scx.http.x.http2;

import cool.scx.common.functional.ScxConsumer;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.error_handler.ScxHttpServerErrorHandler;
import cool.scx.http.x.HttpServerOptions;
import cool.scx.http.x.http2.hpack.HPACKDecoder;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.BufferedInputStreamDataSupplier;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static cool.scx.http.x.http2.Http2Helper.HTTP2_CONNECTION_PREFACE;
import static cool.scx.http.x.http2.Http2SettingsHelper.readHttp2Settings;

//todo 未完成
public class Http2ServerConnection {

    private final static System.Logger LOGGER = System.getLogger(Http2ServerConnection.class.getName());

    private final ScxTCPSocket tcpSocket;
    private final HttpServerOptions options;
    private final ScxConsumer<ScxHttpServerRequest,?> requestHandler;
    private final ScxHttpServerErrorHandler errorHandler;
    private final LinkedDataReader dataReader;
    private final OutputStream dataWriter;
    private final HPACKDecoder hpackDecoder;
    private State state;

    public Http2ServerConnection(ScxTCPSocket tcpSocket, HttpServerOptions options, ScxConsumer<ScxHttpServerRequest,?> requestHandler, ScxHttpServerErrorHandler errorHandler) {
        this.tcpSocket = tcpSocket;
        this.options = options;
        this.requestHandler = requestHandler;
        this.errorHandler = errorHandler;
        this.dataReader = new LinkedDataReader(new BufferedInputStreamDataSupplier(this.tcpSocket.inputStream()));
        this.dataWriter = this.tcpSocket.outputStream();
        this.hpackDecoder = new HPACKDecoder();
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
                var frame = readFrameHeader();

                // 根据帧类型处理
                handleFrameHeader(frame);

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
        var data = dataReader.read(9);
        return Http2FrameHeader.of(data);
    }

    private void handleFrameHeader(Http2FrameHeader frameHeader) throws IOException {
        switch (frameHeader.type()) {
            case DATA -> doData();
            case HEADERS -> doHeaders(frameHeader);
            case PRIORITY -> doPriority();
            case RST_STREAM -> doRstStream();
            case SETTINGS -> doSettings(frameHeader);
            case PUSH_PROMISE -> doPushPromise();
            case PING -> doPing();
            case GOAWAY -> doGoaway();
            case WINDOW_UPDATE -> doWindowUpdate(frameHeader);
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

    private void doWindowUpdate(Http2FrameHeader frameHeader) {
        byte[] payload = dataReader.read(frameHeader.length());

        // WINDOW_UPDATE 帧的有效负载长度应该为 4 字节
        if (payload.length != 4) {
            throw new RuntimeException("WINDOW_UPDATE frame must be 4 bytes, but payload length is " + payload.length);
        }

        // 读取窗口大小增量 (32位无符号整数)
        int windowSizeIncrement = (payload[0] & 0xFF) << 24 |
                (payload[1] & 0xFF) << 16 |
                ((payload[2] & 0xFF) << 8) |
                (payload[3] & 0xFF);

        // 确保窗口大小增量是有效的
        if (windowSizeIncrement == 0) {
            throw new RuntimeException("WINDOW_UPDATE increment must be positive but received " + windowSizeIncrement);
        }

        // 根据 streamId 更新流量控制窗口大小
        if (frameHeader.streamId() == 0) {
            // 更新整个连接的流量控制窗口
            //todo　待实现
//            updateConnectionWindowSize(windowSizeIncrement);
        } else {
            //todo 待实现
            // 更新特定流的流量控制窗口
//            updateStreamWindowSize(frameHeader.streamId(), windowSizeIncrement);
        }
    }

    private void doGoaway() {

    }

    private void doPing() {

    }

    private void doPushPromise() {

    }

    private void doSettings(Http2FrameHeader frameHeader) {
        if (frameHeader.streamId() != 0) {
            throw new RuntimeException("Settings must use stream ID 0, but use " + frameHeader.streamId());
        }

        byte[] payload = dataReader.read(frameHeader.length());

        //此处需要 进行服务器参数设置
        Map<Integer, Integer> integerIntegerMap = readHttp2Settings(payload);
        System.out.println(integerIntegerMap);
        // 发送 ACK 帧以确认接收到 SETTINGS 帧
        sendSettingsAck();
    }

    private void doRstStream() {

    }

    private void doPriority() {

    }

    private void doHeaders(Http2FrameHeader frameHeader) throws IOException {
        byte[] payload = dataReader.read(frameHeader.length());

        // HEADERS 帧有效负载解析
        boolean endHeaders = (frameHeader.flags() & 0x4) != 0;
        boolean padded = (frameHeader.flags() & 0x8) != 0;
        boolean priority = (frameHeader.flags() & 0x20) != 0;

        int offset = 0;

        if (padded) {
            int padLength = payload[offset] & 0xFF;
            offset = offset + 1;
        }

        if (priority) {
            // 读取优先级信息
            int streamDependency = ((payload[offset] & 0x7F) << 24) | ((payload[offset + 1] & 0xFF) << 16) |
                    ((payload[offset + 2] & 0xFF) << 8) | (payload[offset + 3] & 0xFF);
            boolean exclusive = (payload[offset] & 0x80) != 0;
            int weight = payload[offset + 4] & 0xFF;
            offset += 5;

            System.out.println("Priority information: streamDependency=" + streamDependency + ", exclusive=" + exclusive + ", weight=" + weight);
        }

        // 读取头块片段 (Header Block Fragment)
        byte[] headerBlockFragment;
        if (padded) {
            int padLength = payload[frameHeader.length() - 1] & 0xFF;
            headerBlockFragment = Arrays.copyOfRange(payload, offset, payload.length - padLength);
        } else {
            headerBlockFragment = Arrays.copyOfRange(payload, offset, payload.length);
        }

        // 解码头块片段 (Header Block Fragment) 为头部字段
        Map<String, String> headers = hpackDecoder.decode(headerBlockFragment);
        System.out.println("Headers: " + headers);

        if (!endHeaders) {
            // 如果END_HEADERS标志位未设置，需要继续读取CONTINUATION帧
            readContinuationHeaders(frameHeader.streamId(), headers);
        }
    }

    // 解码头块片段的方法
    private Map<String, String> decodeHeaders(byte[] headerBlockFragment) {
        //解码 head

        return null;
    }

    // 继续读取CONTINUATION帧以完成头部字段读取
    private void readContinuationHeaders(int streamId, Map<String, String> headers) throws IOException {
        boolean endHeaders = false;

        while (!endHeaders) {
            Http2FrameHeader continuationFrameHeader = readFrameHeader();

            if (continuationFrameHeader.type() != Http2FrameType.CONTINUATION) {
                throw new RuntimeException("Expected CONTINUATION frame but got " + continuationFrameHeader.type());
            }

            byte[] payload = dataReader.read(continuationFrameHeader.length());
            endHeaders = (continuationFrameHeader.flags() & 0x4) != 0;

            // 解码头块片段并将其添加到头部字段集合
            byte[] headerBlockFragment = Arrays.copyOfRange(payload, 0, payload.length);
            Map<String, String> decodedHeaders = decodeHeaders(headerBlockFragment);
            headers.putAll(decodedHeaders);
        }
    }

    // 发送 ACK 帧以确认接收到 SETTINGS 帧
    private void sendSettingsAck() {
        try {
            // 创建 ACK 帧
            byte[] ackFrame = new byte[9];
            ackFrame[3] = 0x04; // 设置帧类型为 SETTINGS (0x04)
            ackFrame[4] = 0x01; // 标志位设置为 ACK (0x01)

            // 发送 ACK 帧
            dataWriter.write(ackFrame);
            dataWriter.flush();
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "发送 ACK 帧时发生错误", e);
        }
    }

}
