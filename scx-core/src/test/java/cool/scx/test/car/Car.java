package cool.scx.test.car;

import cool.scx.core.base.BaseModel;
import cool.scx.dao.annotation.Column;
import cool.scx.dao.annotation.ScxModel;

@ScxModel(tablePrefix = "test")
public class Car extends BaseModel {

    @Column(unique = true)
    public String name;

    public CarColor color;

    public CarOwner owner;

    public String[] tags;

}
