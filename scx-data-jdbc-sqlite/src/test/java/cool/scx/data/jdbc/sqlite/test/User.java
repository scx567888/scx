package cool.scx.data.jdbc.sqlite.test;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.data.jdbc.annotation.Table;

import java.time.LocalDateTime;

@Table(tablePrefix = "scx_dao")
public class User {
    @Column(primaryKey = true, autoIncrement = true, columnName = "not_id")
    public Long id;
    @Column(columnName = "not_name", unique = true, needIndex = true)
    public String name;
    public Integer age;
    public LocalDateTime createDate;
    public UserInfo userInfo;

    public String[] tags;

    public static class UserInfo {
        public String email;
    }

}
