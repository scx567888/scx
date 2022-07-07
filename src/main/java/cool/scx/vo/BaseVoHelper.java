package cool.scx.vo;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cool.scx.util.JacksonHelper;

/**
 * 仅仅是为了不向外暴露常量
 */
final class BaseVoHelper {

    /**
     * 普通的 jsonMapper 用于向前台发送 Json 数据使用
     */
    static final JsonMapper JSON_MAPPER = JacksonHelper.initJsonMapper();
    /**
     * 普通的 xmlMapper 用于向前台发送 XML 数据使用
     */
    static final XmlMapper XML_MAPPER = JacksonHelper.initXmlMapper();

}
