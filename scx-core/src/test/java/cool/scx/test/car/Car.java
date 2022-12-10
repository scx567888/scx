package cool.scx.test.car;

import cool.scx.core.annotation.Column;
import cool.scx.core.annotation.ScxModel;
import cool.scx.core.base.BaseModel;

@ScxModel(tablePrefix = "test")
public class Car extends BaseModel {

    @Column(unique = true)
    public String name;

    public CarColor color;

    public CarOwner owner;

    public String[] tags;

}
