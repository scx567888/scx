package cool.scx.data.jdbc.mapping;

import cool.scx.jdbc.mapping.Column;
import cool.scx.reflect.FieldInfo;

public interface EntityColumn extends Column {

    FieldInfo javaField();

}
