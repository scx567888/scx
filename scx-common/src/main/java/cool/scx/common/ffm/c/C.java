package cool.scx.common.ffm.c;

import cool.scx.common.ffm.FFMProxy;

public interface C {

    C C = FFMProxy.ffmProxy(null, C.class);

    long strlen(String str);

}
