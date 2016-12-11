package my.wf.samlib.spider.storage;

import my.wf.samlib.spider.model.Author;
import my.wf.samlib.spider.storage.error.StorageException;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuthorStorageImpl implements AuthorStorage {

    private AuthorStore authorStore;
    private DataKeeper dataKeeper;

    public AuthorStorageImpl(DataKeeper dataKeeper) {
        this.dataKeeper = dataKeeper;
        if(null == dataKeeper) {
            throw new StorageException("DataKeeper can not be null");
        }
        this.authorStore = dataKeeper.load();
    }

    @Override
    public Author getAuthor(String url) {
        return authorStore.getAuthors().get(url);
    }

    @Override
    public Author saveAuthor(Author author) {
        authorStore.getAuthors().put(author.getUrl(), author);
        dataKeeper.save(authorStore);
        return author;
    }

    @Override
    public Set<Author> getAllAuthors() {
        return new HashSet<>(authorStore.getAuthors().values());
    }

    @Override
    public Author delete(Author author) {
        authorStore.getAuthors().remove(author.getUrl());
        dataKeeper.save(authorStore);
        return author;
    }

    @Override
    public ZonedDateTime getLastCheckTime() {
        return authorStore.getLastCheckTime();
    }

    @Override
    public void setLastCheckTime(ZonedDateTime lastCheckTime) {
        this.authorStore.setLastCheckTime(lastCheckTime);
    }
}
