package cool.scx.core;

import cool.scx.ansi.Ansi;

/**
 * <p>ScxVersion class.</p>
 *
 * @author scx567888
 * @version 2.0.6
 */
public class ScxVersion {

    /**
     * SCX 版本号
     */
    public static final String SCX_VERSION = "3.1.10";

    /**
     * 在控制台上打印 banner
     */
    static void printBanner() {
        Ansi.ansi()
                .red("   ▄████████ ").green(" ▄████████ ").blue("▀████    ▐████▀ ").ln()
                .red("  ███    ███ ").green("███    ███ ").blue("  ███▌   ████▀  ").ln()
                .red("  ███    █▀  ").green("███    █▀  ").blue("   ███  ▐███    ").ln()
                .red("  ███        ").green("███        ").blue("   ▀███▄███▀    ").ln()
                .red("▀███████████ ").green("███        ").blue("   ████▀██▄     ").ln()
                .red("         ███ ").green("███    █▄  ").blue("  ▐███  ▀███    ").ln()
                .red("   ▄█    ███ ").green("███    ███ ").blue(" ▄███     ███▄  ").ln()
                .red(" ▄████████▀  ").green("████████▀  ").blue("████       ███▄ ").cyan(" Version ").brightCyan(SCX_VERSION).println();
    }

}
