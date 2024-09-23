package cool.scx.common.ffm.platform.c;

import cool.scx.common.ffm.FFMProxy;

public interface C {

    C C = FFMProxy.ffmProxy(C.class);

    long strlen(String str);

}
