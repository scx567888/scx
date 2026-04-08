package cool.scx.ffm.platform.c;

import cool.scx.ffm.ScxFFM;

/// 提供一些 C 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface C {

    C C = ScxFFM.ffmProxy(C.class);

    long strlen(String str);

    int abs(int x);

    double sin(double x);

    double sqrt(double x);

}
