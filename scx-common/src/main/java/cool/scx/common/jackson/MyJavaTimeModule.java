package cool.scx.common.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import cool.scx.common.constant.ScxDateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/// 获取针对日期处理的 jackson module;
///   
/// 仅仅是在 jackson-datatype-jsr310 包的基础上 添加了一些自定义的日期序列化格式
///
/// @author scx567888
/// @version 0.0.1
public class MyJavaTimeModule extends SimpleModule {

    public MyJavaTimeModule() {
        //因为其内部默认使用 ISO-8601 标准 作为日期处理格式
        //如 DateTimeFormatter.ISO_LOCAL_DATE_TIME , DateTimeFormatter.ISO_LOCAL_TIME
        //但是这里我们需要针对一些 常见的日期格式 如 [LocalDateTime] 进行更友好的序列化格式处理 所以这里使用 自定义的 DateTimeFormatter

        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ScxDateTimeFormatter.yyyy_MM_dd_HH_mm_ss));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ScxDateTimeFormatter.yyyy_MM_dd_HH_mm_ss));

        this.addSerializer(LocalDate.class, new LocalDateSerializer(ScxDateTimeFormatter.yyyy_MM_dd));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(ScxDateTimeFormatter.yyyy_MM_dd));

        this.addSerializer(LocalTime.class, new LocalTimeSerializer(ScxDateTimeFormatter.HH_mm_ss));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ScxDateTimeFormatter.HH_mm_ss));
    }

}
