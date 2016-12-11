package my.wf.samlib.spider.spider.error;

import my.wf.samlib.spider.error.BaseException;

public class SpiderException extends BaseException {

    private String url;

    public SpiderException(String url, String message, Throwable cause) {
        super(message, cause);
        this.url = url;
    }
}
