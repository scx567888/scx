package cool.scx.http.header.cookie;

/// CookieSameSite
///
/// @author scx567888
/// @version 0.0.1
public enum CookieSameSite {

    NONE("None"),
    STRICT("Strict"),
    LAX("Lax");

    private final String value;

    CookieSameSite(String label) {
        this.value = label;
    }

    /// @param attrValue a
    /// @return 未找到会抛出异常
    public static CookieSameSite of(String attrValue) {
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

    /// @param attrValue a
    /// @return 未找到会返回 null
    public static CookieSameSite find(String attrValue) {
        if ("None".equalsIgnoreCase(attrValue)) {
            return NONE;
        }
        if ("Strict".equalsIgnoreCase(attrValue)) {
            return STRICT;
        }
        if ("Lax".equalsIgnoreCase(attrValue)) {
            return LAX;
        }
        return null;
    }

    public String value() {
        return value;
    }

}
