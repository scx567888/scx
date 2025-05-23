package cool.scx.data.aggregation;

import cool.scx.data.build_control.BuildControl;
import cool.scx.data.build_control.BuildControlInfo;

import static cool.scx.data.build_control.BuildControlInfo.ofInfo;

public non-sealed class FieldGroupBy extends GroupBy {

    private final String fieldName;
    private final BuildControlInfo info;

    public FieldGroupBy(String fieldName, BuildControlInfo info) {
        //名称不能为空
        if (fieldName == null) {
            throw new NullPointerException("GroupBy 参数错误 : fieldName 不能为空 !!!");
        }
        this.fieldName = fieldName;
        this.info = info;
    }

    public FieldGroupBy(String fieldName,  BuildControl... controls) {
        this(fieldName,  ofInfo(controls));
    }

    public String fieldName() {
        return fieldName;
    }

    public BuildControlInfo info() {
        return info;
    }
    
}
