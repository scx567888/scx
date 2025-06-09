package cool.scx.data.jdbc.mapping;

import cool.scx.jdbc.mapping.type.TypeColumn;
import cool.scx.reflect.FieldInfo;

public interface EntityColumn extends TypeColumn {

    FieldInfo javaField();
    
}
