package cool.scx.test;

import cool.scx.dao.annotation.Column;

import java.time.LocalDateTime;

public class User {
    @Column(primaryKey = true, autoIncrement = true)
    public Long id;
    public String name;
    public Integer age;
    public LocalDateTime createDate;
}
