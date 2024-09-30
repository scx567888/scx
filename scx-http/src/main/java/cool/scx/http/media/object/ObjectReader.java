package cool.scx.http.media.object;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static cool.scx.http.media.string.StringReader.STRING_READER;

public class ObjectReader<T> implements MediaReader<T> {

    private final JavaType type;

    public ObjectReader(Class<T> clazz) {
        this.type = ObjectUtils.constructType(clazz);
    }

    public ObjectReader(TypeReference<T> clazz) {
        this.type = ObjectUtils.constructType(clazz);
    }

    public ObjectReader(JavaType clazz) {
        this.type = clazz;
    }

    @Override
    public T read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        var str = STRING_READER.read(inputStream, requestHeaders);
        var contentType = requestHeaders.contentType();
        var mediaType = contentType != null ? contentType.mediaType() : null;
        //猜测一下
        return switch (mediaType) {
            case APPLICATION_JSON -> readJson(str);
            case APPLICATION_XML -> readXml(str);
            case null, default -> tryReadOrTextNode(str);
        };
    }

    public T readJson(String jsonStr) {
        try {
            return jsonMapper().readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    public T readXml(String xmlStr) {
        try {
            return xmlMapper().readValue(xmlStr, type);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    public T tryReadOrTextNode(String str) {
        try { //先尝试以 json 格式进行尝试转换
            return jsonMapper().readValue(str, type);
        } catch (Exception exception) {
            try {//再尝试以 xml 的格式进行转换
                return xmlMapper().readValue(str, type);
            } catch (JsonProcessingException e) {
                // json 和 xml 均转换失败 直接报错
                throw new BadRequestException();
            }
        }
    }

}
