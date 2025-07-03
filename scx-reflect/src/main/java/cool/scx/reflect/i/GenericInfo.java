package cool.scx.reflect.i;

/// 表示泛型
public interface GenericInfo {
    
    /// 泛型名称
    String name();
    
    /// 上界
    ClassInfo[] upperBounds();
    
    /// 下界
    ClassInfo[] lowerBounds();
    
    /// 真实类型
    ClassInfo actualType();
    
}
