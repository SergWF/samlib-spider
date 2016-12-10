package my.wf.samlib.spider.storage;

import my.wf.samlib.spider.model.Author;

import java.util.Set;

public interface AuthorStorage {
    Author getAuthor(String url);
    Author saveAuthor(Author author);
    Set<Author> getAllAuthors();
    Author delete(Author author);
}
