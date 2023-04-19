package cool.scx.data.test;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.data.jdbc.annotation.Table;
import cool.scx.data.mysql_x.annotation.Collection;

import java.time.LocalDateTime;

@Collection(prefix = "scx_dao_mysql_x")
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

    static class UserInfo {
        public String email;
    }

}
