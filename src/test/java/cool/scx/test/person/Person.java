package cool.scx.test.person;

import cool.scx.core.annotation.ScxModel;
import cool.scx.core.base.BaseModel;

@ScxModel(tablePrefix = "scx")
public class Person extends BaseModel {
    /**
     * 关联的 汽车 ID
     */
    public Long carID;

    /**
     * 年龄
     */
    public Integer age;

}
