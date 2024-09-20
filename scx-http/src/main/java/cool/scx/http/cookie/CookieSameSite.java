package cool.scx.http.cookie;

public enum CookieSameSite {

    NONE("None"),

    STRICT("Strict"),

    LAX("Lax");

    private final String value;

    CookieSameSite(String label) {
        this.value = label;
    }

    public String value() {
        return value;
    }

}
