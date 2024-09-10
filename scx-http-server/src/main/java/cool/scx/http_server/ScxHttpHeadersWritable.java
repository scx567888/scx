package cool.scx.http_server;

public interface ScxHttpHeadersWritable extends ScxHttpHeaders {

    ScxHttpHeadersWritable set(ScxHttpHeader header);
    
    ScxHttpHeadersWritable add(ScxHttpHeader header);
    
    ScxHttpHeadersWritable set(ScxHttpHeaderName headerName,String headerValue);
    
    ScxHttpHeadersWritable add(ScxHttpHeaderName headerName,String headerValue);
    
}
