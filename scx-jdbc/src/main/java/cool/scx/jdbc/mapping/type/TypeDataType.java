package cool.scx.jdbc.mapping.type;

import cool.scx.jdbc.mapping.DataType;
import cool.scx.jdbc.standard.StandardDataType;

public interface TypeDataType extends DataType {

    StandardDataType standardDataType();

}
