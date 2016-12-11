package my.wf.samlib.spider.storage.error;

import my.wf.samlib.spider.error.BaseException;

public class StorageException extends BaseException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
