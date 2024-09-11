package cool.scx.http;

import java.io.InputStream;

public interface ScxHttpBody {

    InputStream asInputStream();

    byte[] asBytes();

    String asString();

    ScxHttpFormData asFormData();

}
