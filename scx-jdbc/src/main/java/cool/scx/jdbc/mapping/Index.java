package cool.scx.jdbc.mapping;

/// 索引
///
/// @author scx567888
/// @version 0.0.1
public interface Index {

    String name();

    String columnName();

    boolean unique();

}
