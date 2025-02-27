package cool.scx.jdbc.mapping;

/// 键
///
/// @author scx567888
/// @version 0.0.1
public interface Key {

    String name();

    String columnName();

    boolean primary();

}
