package cool.scx.ffm.platform.c;

import cool.scx.ffm.FFMProxy;

public interface C {

    C C = FFMProxy.ffmProxy(C.class);

    long strlen(String str);

}
