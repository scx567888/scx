package cool.scx.http.x.http1.headers.upgrade;

record ScxUpgradeImpl(String value) implements ScxUpgrade {

    ScxUpgradeImpl {
        value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }

}
