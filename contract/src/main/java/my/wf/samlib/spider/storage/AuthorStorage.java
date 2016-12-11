package my.wf.samlib.spider.storage;

import my.wf.samlib.spider.engine.SpiderEngine;
import my.wf.samlib.spider.model.Author;

import java.time.ZonedDateTime;
import java.util.Set;

public interface AuthorStorage {
    Author getAuthor(String url);
    Author saveAuthor(Author author);
    Set<Author> getAllAuthors();
    Author delete(Author author);
    ZonedDateTime getLastCheckTime();
    void setLastCheckTime(ZonedDateTime lastCheckTime);
}
