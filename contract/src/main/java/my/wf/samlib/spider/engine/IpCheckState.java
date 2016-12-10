package my.wf.samlib.spider.engine;

public class IpCheckState {
    private String ip;
    private boolean inSpamList;
    private boolean blocked;
    private boolean otherError;
    private String info;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isInSpamList() {
        return inSpamList;
    }

    public void setInSpamList(boolean inSpamList) {
        this.inSpamList = inSpamList;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isOtherError() {
        return otherError;
    }

    public void setOtherError(boolean otherError) {
        this.otherError = otherError;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isAccessible() {
        return !blocked && !inSpamList && !otherError;
    }

    @Override
    public String toString() {
        return "IpCheckState{" + "ip='" + ip + '\'' + ", inSpamList=" + inSpamList + ", blocked=" + blocked +
                ", otherError=" + otherError + ", info='" + info + '\'' + '}';
    }
}
