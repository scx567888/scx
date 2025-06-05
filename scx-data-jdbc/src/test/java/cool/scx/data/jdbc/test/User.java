package cool.scx.data.jdbc.test;

import cool.scx.data.jdbc.annotation.Column;

public class User {

    @Column(primary = true, autoIncrement = true)
    public Long id;

    @Column(defaultValue = "0")
    public Long money;

    public String name;

}
