package cool.scx.orm;

import cool.scx.orm.jdbc.mapping.Column;

import java.lang.reflect.Field;

/**
 * Column Field 之间的映射
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnMapping extends Column {

    Field javaField();

    default Object javaFieldValue(Object target) {
        try {
            return this.javaField().get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
