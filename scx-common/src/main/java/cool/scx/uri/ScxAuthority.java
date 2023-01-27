package cool.scx.uri;

public class ScxAuthority {
    String userInfo;
    String host;
    int port;

    public ScxAuthority(String userInfo, String host, int port) {
        this.userInfo=userInfo;
        this.host=host;
        this.port=port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        var str="";
        if (userInfo!=null){
            str+=userInfo;
        }
        if (host!=null){
            str+=host;
        }
        if (port!=-1){
            str+=":"+host;
        }
        return str;
    }

}
