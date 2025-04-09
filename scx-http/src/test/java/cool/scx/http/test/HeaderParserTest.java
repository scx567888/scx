package cool.scx.http.test;

import cool.scx.http.headers.ScxHttpHeaders;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

public class HeaderParserTest {

    public static void main(String[] args) {
        testBasicValidHeader();
        testKeyAndValueWithSpaces();
        testEmptyLine();
        testMissingColon();
        testValueWithCRLF();
        testMultipleNewlines();
        testNonStandardLineEndings();
        testSpecialCharactersInValue();
        testEmptyHeader();
    }

    @Test
    public static void testBasicValidHeader() {
        String headersStr = "Content-Type: application/json\nX-Request-ID: 12345\n";
        var headers = ScxHttpHeaders.of(headersStr);  // 假设你有这个实现

        assertEquals(headers.get("Content-Type"), "application/json");
        assertEquals(headers.get("X-Request-ID"), "12345");
    }

    @Test
    public static void testKeyAndValueWithSpaces() {
        String headersStr = "  Content-Type   :   application/json   \n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Type"), "application/json");
    }

    @Test
    public static void testEmptyLine() {
        String headersStr = "Content-Type: application/json\n\nX-Request-ID: 12345\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Type"), "application/json");
        assertEquals(headers.get("X-Request-ID"), "12345");
    }

    @Test
    public static void testMissingColon() {
        String headersStr = "InvalidHeaderLine\nContent-Type: application/json\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertNull(headers.get("InvalidHeaderLine"));
        assertEquals(headers.get("Content-Type"), "application/json");
    }

    @Test
    public static void testValueWithCRLF() {
        String headersStr = "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\\r\\n\"\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Disposition"), "form-data; name=\"file\"; filename=\"example.txt\\r\\n\"");
    }

    @Test
    public static void testMultipleNewlines() {
        String headersStr = "Content-Type: application/json\n\n\nX-Request-ID: 12345\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Type"), "application/json");
        assertEquals(headers.get("X-Request-ID"), "12345");
    }

    @Test
    public static void testNonStandardLineEndings() {
        String headersStr = "Content-Type: application/json\r\nX-Request-ID: 12345\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Type"), "application/json");
        assertEquals(headers.get("X-Request-ID"), "12345");
    }

    @Test
    public static void testSpecialCharactersInValue() {
        String headersStr = "Set-Cookie: sessionId=abc123; path=/; domain=.example.com\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Set-Cookie"), "sessionId=abc123; path=/; domain=.example.com");
    }

    @Test
    public static void testEmptyHeader() {
        String headersStr = "Content-Type: \nX-Request-ID: 12345\n";
        var headers = ScxHttpHeaders.of(headersStr);

        assertEquals(headers.get("Content-Type"), "");
        assertEquals(headers.get("X-Request-ID"), "12345");
    }
    
}
