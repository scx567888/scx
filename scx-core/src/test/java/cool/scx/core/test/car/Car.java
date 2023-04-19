package cool.scx.core.test.car;

import cool.scx.core.base.BaseModel;
import cool.scx.data.jdbc.annotation.Column;
import cool.scx.data.jdbc.annotation.Table;

@Table(tablePrefix = "test")
public class Car extends BaseModel {

    @Column(unique = true)
    public String name;

    public CarColor color;

    public CarOwner owner;

    public String[] tags;

}
