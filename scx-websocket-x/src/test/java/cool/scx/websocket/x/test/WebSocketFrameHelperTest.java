package cool.scx.websocket.x.test;

import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.exception.NoMoreDataException;
import cool.scx.byte_reader.supplier.ByteArrayByteSupplier;
import cool.scx.websocket.WebSocketOpCode;
import cool.scx.websocket.x.WebSocketProtocolFrame;
import cool.scx.websocket.x.WebSocketProtocolFrameHelper;
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
        var reader = new ByteReader(new ByteArrayByteSupplier(frameData));
        var frame = WebSocketProtocolFrameHelper.readFrame(reader, Integer.MAX_VALUE);

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
        var frame = WebSocketProtocolFrame.of(true, WebSocketOpCode.TEXT, new byte[]{'H', 'e', 'l', 'l', 'o'});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WebSocketProtocolFrameHelper.writeFrame(frame, out);
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
        var reader = new ByteReader(new ByteArrayByteSupplier(frameData));
        var frame = WebSocketProtocolFrameHelper.readFrameUntilLast(reader, Integer.MAX_VALUE, Integer.MAX_VALUE);

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
