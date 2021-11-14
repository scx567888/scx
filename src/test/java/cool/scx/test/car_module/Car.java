package cool.scx.test.car_module;

import cool.scx.annotation.Column;
import cool.scx.annotation.ScxModel;
import cool.scx.base.BaseModel;

@ScxModel
public class Car extends BaseModel {
    @Column(unique = true)
    public String name;
}
