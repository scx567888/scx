package cool.scx.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import cool.scx.common.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

import static cool.scx.common.jackson.FieldFilter.FilterMode.INCLUDED;

public class DeepFieldFilter extends SimpleBeanPropertyFilter {

    private final FieldFilter fieldFilter;

    public DeepFieldFilter(FieldFilter fieldFilter) {
        this.fieldFilter = fieldFilter;
    }

    public static boolean match(String[] e1, String[] e2) {
        if (e1.length != e2.length) {
            return false;
        }
        for (int i = 0; i < e1.length; i = i + 1) {
            var b = match(e1[i], e2[i]);
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static boolean match(String e1, String e2) {
        return e1.equals(e2) || e1.equals("*");
    }

    private String[] getFullPath(PropertyWriter writer, JsonGenerator jsonGenerator) {
        var name = writer.getName();
        var context = jsonGenerator.getOutputContext();
        var jsonPointer = context.getParent().pathAsPointer(false);
        var string = jsonPointer.toString();
        var split = Arrays.stream(string.split("/")).filter(StringUtils::notBlank).collect(Collectors.toList());
        split.add(name);
        return split.toArray(String[]::new);
    }

    public boolean matchAny(String[] fullPath) {
        for (var fieldName : fieldFilter.getFieldNames()) {
            if (match(fieldName.split("\\."), fullPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        var fullPath = getFullPath(writer, jgen);
        var matched = matchAny(fullPath);
        boolean f = (fieldFilter.getFilterMode() == INCLUDED) == matched;
        if (f) {
            writer.serializeAsField(pojo, jgen, provider);
        } else {
            writer.serializeAsOmittedField(pojo, jgen, provider);
        }
    }

}
