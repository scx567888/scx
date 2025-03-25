package cool.scx.http.x.http1.headers.upgrade;

public sealed interface ScxUpgrade permits Upgrade, ScxUpgradeImpl {

    static ScxUpgrade of(String v) {
        // 优先使用 Upgrade
        var m = Upgrade.find(v);
        return m != null ? m : new ScxUpgradeImpl(v);
    }

    String value();

}
