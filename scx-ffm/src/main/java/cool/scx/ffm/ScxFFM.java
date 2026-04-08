package cool.scx.ffm;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

import static java.lang.foreign.Arena.global;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.SymbolLookup.libraryLookup;

public final class ScxFFM {

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(nativeLinker().defaultLookup()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(String name, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(libraryLookup(name, global())));
    }

    @SuppressWarnings("unchecked")
    public static <T> T ffmProxy(Path path, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(libraryLookup(path, global())));
    }

}
