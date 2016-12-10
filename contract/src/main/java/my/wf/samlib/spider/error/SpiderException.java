package my.wf.samlib.spider.error;

public class SpiderException extends RuntimeException {

    private String url;

    public SpiderException(String url, String message, Throwable cause) {
        super(message, cause);
        this.url = url;
    }
}
