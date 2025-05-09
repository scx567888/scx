package cool.scx.bean.annotation;

import java.lang.annotation.*;

/// 标识注册时优先构造函数
@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferredConstructor {
    
}
