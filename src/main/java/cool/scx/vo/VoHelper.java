package cool.scx.vo;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cool.scx.util.JacksonHelper;

/**
 * vo helper
 *
 * @author scx567888
 * @version 1.4.4
 */
public final class VoHelper {

    /**
     * 普通的 jsonMapper 用于向前台发送 Json 数据使用
     */
    static JsonMapper JSON_MAPPER = JacksonHelper.initJsonMapper();
    /**
     * 普通的 xmlMapper 用于向前台发送 XML 数据使用
     */
    static XmlMapper XML_MAPPER = JacksonHelper.initXmlMapper();

}
