package cool.scx.jdbc.mapping.type;

import cool.scx.jdbc.mapping.Column;

/**
 * 具有 java 类型 的 Column
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface TypeColumn extends Column {

    @Override
    TypeDataType dataType();

}
