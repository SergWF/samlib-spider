package my.wf.samlib.spider.engine;

import my.wf.samlib.spider.model.Author;
import my.wf.samlib.spider.model.Changes;
import my.wf.samlib.spider.model.WritingData;
import my.wf.samlib.spider.spider.Spider;
import my.wf.samlib.spider.storage.AuthorStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

public class SpiderEngine {

    private static final Logger logger = LoggerFactory.getLogger(SpiderEngine.class);

    private Spider spider;
    private AuthorStorage authorStorage;
    private String accessCheckPageUrl;

    public SpiderEngine(Spider spider, AuthorStorage authorStorage, String accessCheckPageUrl) {
        this.spider = spider;
        this.authorStorage = authorStorage;
        this.accessCheckPageUrl = accessCheckPageUrl;
    }

    AuthorChangeHandler getAuthorChangeHandler(Author author) {
        return new AuthorChangeHandler(author, LocalDateTime.now());
    }

    Author updateAuthor(Author author) {
        logger.debug("check author's url {}", author.getUrl());
        AuthorChangeHandler authorChangeHandler = getAuthorChangeHandler(author);
        Set<WritingData> writingDatas = spider.readAndParseAuthorsPage(author.getUrl());
        logger.debug("found {} writings on the page", writingDatas.size());
        writingDatas.forEach(authorChangeHandler::handleWritingUpdate);
        authorChangeHandler.handleDeleted(author, writingDatas);
        logUpdateStatistic(author);
        return authorStorage.saveAuthor(author);
    }

    private void logUpdateStatistic(Author author) {
        int newWritings =
                (int) author.getWritings().stream().filter(writing -> writing.getChanges().contains(Changes.NEW))
                            .count();
        int updatedWritings =
                (int) author.getWritings().stream().filter(writing -> !writing.getChanges().isEmpty()).count();
        if (updatedWritings > 0) {
            logger.info("author {}: updated {} with {} new", author.getName(), updatedWritings, newWritings);
        }
    }

    boolean checkServerAccessibility() {
        if (null == accessCheckPageUrl || accessCheckPageUrl.trim().isEmpty()) {
            logger.warn("URL for checking server not found");
            return true;
        } else {
            IpCheckState ipCheckState = spider.readAndParseAccessCheckPage(accessCheckPageUrl);
            if (!ipCheckState.isAccessible()) {
                logger.error("Access to server denied: {}", ipCheckState.toString());
            }
            return ipCheckState.isAccessible();
        }
    }

    public void checkAllAuthors() {
        logger.debug("check ipstate");
        if (!checkServerAccessibility()) {
            logger.warn("Checking cancelled");
            return;
        }
        updateAllAuthors();
    }

    void updateAllAuthors() {
        logger.info("start author checking");
        authorStorage.setLastCheckTime(ZonedDateTime.now());
        authorStorage.getAllAuthors().forEach(this::updateAuthor);
        logger.info("end author checking");
    }

    public Author addAuthor(String url) {
        Author author = authorStorage.getAuthor(url);
        if (null != author) {
            logger.warn("author with url {} already exists: {}", url, author.getName());
            return author;
        }
        author = new Author();
        author.setUrl(url);
        logger.info("New author added: {}", url);
        return authorStorage.saveAuthor(author);
    }

    public Author removeAuthor(String url) {
        Author author = authorStorage.getAuthor(url);
        if (null == author) {
            logger.warn("wrong author's url {} for removing", url);
        } else {
            authorStorage.delete(author);
            logger.info("author {} removed", author.getName());
        }
        return author;
    }

    public Author getAuthor(String url) {
        logger.debug("read author {} from storage", url);
        return authorStorage.getAuthor(url);
    }
}
