package cool.scx.uri;

import javax.management.Query;
import java.net.URI;
import java.net.URL;

public class ScxURI {

    public String scheme;
    public ScxAuthority authority;
    public String path;
    public ScxQuery query;
    public String fragment;

    public ScxURI(String str) {
        var uri = URI.create(str);
        this.scheme=uri.getScheme();
        this.authority=new ScxAuthority(uri.getUserInfo(),uri.getHost(),uri.getPort());
        this.path=uri.getPath();
        this.query=new ScxQuery(uri.getQuery());
        this.fragment=uri.getFragment();
    }

    private ScxAuthority getAuthority() {
        return this.authority;
    }

    private String getUserInfo() {
        return this.authority.userInfo;
    }

    private String getFragment() {
        return fragment;
    }

    private String getPath() {
        return path;
    }

    public ScxURI(URI uri) {
        this.scheme = scheme;
    }

    public ScxURI(URL url) {
        this.scheme = scheme;
    }

    public ScxURI(ScxURI scxURI) {
        this.scheme = scheme;
    }


    public String getHost() {
        return authority.getHost();
    }

    public void setHost(String host) {
        authority.setHost(host);
    }

    public int getPort() {
        return authority.getPort();
    }

    public void setPort(int port) {
        authority.setPort(port);
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public ScxQuery getQuery() {
        return query;
    }

    public void setQuery(ScxQuery query) {
        this.query = query;
    }

    public static void main(String[] args) {
        URI uri = URI.create("http:www.baidu.com/user/info?a=b&c=d#head");
        var ur1 = new ScxURI("http:www.baidu.com/user/info?a=b&c=d#head");
        String query1 = uri.getQuery();
        String rawQuery = uri.getRawQuery();
        ScxQuery query2 = ur1.getQuery();

        int port = uri.getPort();
        int port1 = ur1.getPort();

        String host = uri.getHost();
        String host1 = ur1.getHost();

        String scheme1 = uri.getScheme();
        String scheme2 = ur1.getScheme();

        String path1 = uri.getPath();
        String rawPath = uri.getRawPath();
        String path2 = ur1.getPath();

        String fragment1 = uri.getFragment();
        String rawFragment = uri.getRawFragment();
        String fragment2 = ur1.getFragment();

        String userInfo = uri.getUserInfo();
        String rawUserInfo = uri.getRawUserInfo();
        String userInfo1 = ur1.getUserInfo();

        String authority1 = uri.getAuthority();
        String rawAuthority = uri.getRawAuthority();
        ScxAuthority authority2 = ur1.getAuthority();
        System.out.println();
    }

    @Override
    public String toString() {
        return scheme+"://"+authority+path+"?"+query+"#"+fragment;
    }

}
