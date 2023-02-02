package cool.scx.test;

import cool.scx.dao.annotation.Column;
import cool.scx.dao.annotation.Table;

import java.time.LocalDateTime;

@Table(tablePrefix = "scx_dao")
public class User {
    @Column(primaryKey = true, autoIncrement = true, columnName = "not_id")
    public Long id;
    @Column(columnName = "not_name")
    public String name;
    public Integer age;
    public LocalDateTime createDate;
}
