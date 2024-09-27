package cool.scx.http.cookie;

public enum CookieSameSite {

    NONE("None"),

    STRICT("Strict"),

    LAX("Lax");

    private final String value;

    CookieSameSite(String label) {
        this.value = label;
    }

    public static CookieSameSite of(String attrValue) {
        if (attrValue == null) {
            return null;    
        }
        if ("None".equalsIgnoreCase(attrValue)) {
            return NONE;
        }
        if ("Strict".equalsIgnoreCase(attrValue)) {
            return STRICT;
        }
        if ("Lax".equalsIgnoreCase(attrValue)) {
            return LAX;
        }
        throw new IllegalArgumentException("Unknown cookie same site: " + attrValue);
    }

    public String value() {
        return value;
    }

}
