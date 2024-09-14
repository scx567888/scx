package cool.scx.http;

public interface ScxHttpHeadersWritable extends ScxHttpHeaders {

    ScxHttpHeadersWritable set(ScxHttpHeaderName headerName, String... headerValue);

    ScxHttpHeadersWritable set(String headerName, String... headerValue);

    ScxHttpHeadersWritable add(ScxHttpHeaderName headerName, String... headerValue);

    ScxHttpHeadersWritable add(String headerName, String... headerValue);

    ScxHttpHeadersWritable remove(ScxHttpHeaderName headerName);

    ScxHttpHeadersWritable remove(String headerName);

}
