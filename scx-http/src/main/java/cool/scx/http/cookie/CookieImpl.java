package cool.scx.http.cookie;

class CookieImpl implements CookieWritable {

    private String name;
    private String value;
    private String domain;
    private String path;
    private long maxAge;
    private boolean secure;
    private boolean httpOnly;
    private CookieSameSite sameSite;

    public CookieImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public CookieWritable domain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public CookieWritable path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public CookieWritable maxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    @Override
    public CookieWritable secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    @Override
    public CookieWritable httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    @Override
    public CookieWritable sameSite(CookieSameSite sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String domain() {
        return domain;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public long maxAge() {
        return maxAge;
    }

    @Override
    public boolean secure() {
        return secure;
    }

    @Override
    public boolean httpOnly() {
        return httpOnly;
    }

    @Override
    public CookieSameSite sameSite() {
        return sameSite;
    }

    @Override
    public String toString() {
        var buf = new StringBuilder()
                .append(name)
                .append('=')
                .append(value);
        if (domain != null) {
            buf.append(", domain=")
                    .append(domain);
        }
        if (path != null) {
            buf.append(", path=")
                    .append(path);
        }
        if (maxAge >= 0) {
            buf.append(", maxAge=")
                    .append(maxAge)
                    .append('s');
        }
        if (secure) {
            buf.append(", secure");
        }
        if (httpOnly) {
            buf.append(", HTTPOnly");
        }
        if (sameSite != null) {
            buf.append(", SameSite=").append(sameSite.value());
        }
        return buf.toString();
    }

}
