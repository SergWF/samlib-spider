package my.wf.samlib.spider.storage;

public interface DataKeeper {

    AuthorStore load();
    void save(AuthorStore authorStore);
}
