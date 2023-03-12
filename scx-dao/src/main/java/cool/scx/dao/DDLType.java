package cool.scx.dao;

public interface DDLType {

    String getRawTypeName();

    default String getTypeName(Long size) {
        return size == null ? getRawTypeName() : getRawTypeName() + "(" + size + ")";
    }

}
