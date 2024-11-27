package cool.scx.http.test;

import cool.scx.http.web_socket.WebSocketFrame;
import cool.scx.http.web_socket.WebSocketFrameHelper;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.ByteArrayDataReader;
import cool.scx.io.NoMoreDataException;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.testng.Assert.*;

public class WebSocketFrameHelperTest {

    public static void main(String[] args) throws IOException {
        testReadFrame();
        testWriteFrame();
        testReadFrameUntilLast();
    }

    @Test
    public static void testReadFrame() throws NoMoreDataException, IOException {
        byte[] frameData = {
                (byte) 0b1000_0001, // FIN + Text frame
                (byte) 0b0000_0101, // No mask, payload length = 5
                'H', 'e', 'l', 'l', 'o'
        };
        ByteArrayDataReader reader = new ByteArrayDataReader(frameData);
        WebSocketFrame frame = WebSocketFrameHelper.readFrame(reader);

        assertTrue(frame.fin());
        assertFalse(frame.rsv1());
        assertFalse(frame.rsv2());
        assertFalse(frame.rsv3());
        assertEquals(frame.opCode(), WebSocketOpCode.TEXT);
        assertFalse(frame.masked());
        assertEquals(frame.payloadLength(), 5);
        assertEquals(frame.payloadData(), new byte[]{'H', 'e', 'l', 'l', 'o'});
    }

    @Test
    public static void testWriteFrame() throws IOException {
        WebSocketFrame frame = WebSocketFrame.of(true, WebSocketOpCode.TEXT, new byte[]{'H', 'e', 'l', 'l', 'o'});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WebSocketFrameHelper.writeFrame(frame, out);
        byte[] expectedFrame = {
                (byte) 0b1000_0001, // FIN + Text frame
                (byte) 0b0000_0101, // No mask, payload length = 5
                'H', 'e', 'l', 'l', 'o'
        };
        assertEquals(expectedFrame, out.toByteArray());
    }

    @Test
    public static void testReadFrameUntilLast() throws NoMoreDataException, IOException {
        byte[] frameData = {
                (byte) 0b0000_0001, // Text frame, not final
                (byte) 0b0000_0011, // No mask, payload length = 3
                'H', 'e', 'l',
                (byte) 0b1000_0000, // Continuation frame, final
                (byte) 0b0000_0010, // No mask, payload length = 2
                'l', 'o'
        };
        ByteArrayDataReader reader = new ByteArrayDataReader(frameData);
        WebSocketFrame frame = WebSocketFrameHelper.readFrameUntilLast(reader);

        assertTrue(frame.fin());
        assertFalse(frame.rsv1());
        assertFalse(frame.rsv2());
        assertFalse(frame.rsv3());
        assertEquals(frame.opCode(), WebSocketOpCode.TEXT);
        assertFalse(frame.masked());
        assertEquals(frame.payloadLength(), 5);
        assertEquals(frame.payloadData(), new byte[]{'H', 'e', 'l', 'l', 'o'});
    }

}
