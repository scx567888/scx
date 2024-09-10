package cool.scx.http_server;

import io.helidon.common.parameters.Parameters;
import io.helidon.http.media.multipart.MultiPart;

import java.io.InputStream;

public interface ScxHttpBody {

    InputStream asInputStream();

    byte[] asBytes();

    String asString();

    /**
     * todo 此处应该返回 Scx 包装类
     * @return a
     */
    MultiPart asMultiPart();

    /**
     * todo 此处应该返回 Scx 包装类
     * @return a
     */
    Parameters asFormData();

}
