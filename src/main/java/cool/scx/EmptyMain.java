package cool.scx;

import cool.scx.util.ConsoleUtils;

/**
 * EmptyMain 空启动类
 *
 * @author scx567888
 * @version 1.1.9
 */
public final class EmptyMain {

    /**
     * 空方法
     *
     * @param args a
     */
    public static void main(String[] args) {
        Scx.printBanner();
        System.out.println("请在您的模块中使用, 按下回车退出!!!");
        ConsoleUtils.readLine();
    }

}
