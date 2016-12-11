package my.wf.samlib.spider.storage;

import my.wf.samlib.spider.model.Author;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorStore {
    Map<String, Author> authors = new ConcurrentHashMap<>();
    private ZonedDateTime lastCheckTime;

    public Map<String, Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Map<String, Author> authors) {
        this.authors = authors;
    }

    public ZonedDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(ZonedDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
