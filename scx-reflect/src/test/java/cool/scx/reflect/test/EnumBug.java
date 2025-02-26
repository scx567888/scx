package cool.scx.reflect.test;

import cool.scx.reflect.ClassType;
import cool.scx.reflect.ReflectHelper;

/// isEnum 判断并不准确 这里测试 ClassInfo 是否能够正确处理这种情况
public enum EnumBug {
    Alpha(3),
    Beta(6),
    Delta(4) {
        @Override
        public int getValue() {
            return -1;
        }

        @Override
        public String toString() {
            return "Gamma";
        }
    },
    Epsilon(9);

    private int value;

    EnumBug(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        for (var thing : EnumBug.values()) {
            String nameString = thing + " (" + thing.name() + ")  ";
            var a = thing.getClass().isEnum();
            var b = ReflectHelper.getClassInfo(thing.getClass()).classType() == ClassType.ENUM;
            System.out.println(nameString + " isEnum = " + a + " ClassInfo = " + b);
        }
    }

    public int getValue() {
        return this.value;
    }

}
