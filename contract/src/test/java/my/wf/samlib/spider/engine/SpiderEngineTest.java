package my.wf.samlib.spider.engine;

import my.wf.samlib.spider.model.Author;
import my.wf.samlib.spider.model.WritingData;
import my.wf.samlib.spider.spider.Spider;
import my.wf.samlib.spider.storage.AuthorStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpiderEngineTest {

    private SpiderEngine spiderEngine;
    private AuthorStorage authorStorage;
    private Spider spider;
    private AuthorChangeHandler authorChangeHandler;
    private IpCheckState ipCheckState;
    private Author author;

    private String authorUrl = "http://url";

    @Before
    public void setUp() throws Exception {
        authorStorage = Mockito.mock(AuthorStorage.class);
        spider = Mockito.mock(Spider.class);
        spiderEngine = Mockito.spy(new SpiderEngine());
        authorChangeHandler = Mockito.mock(AuthorChangeHandler.class);
        spiderEngine.setAuthorStorage(authorStorage);
        spiderEngine.setSpider(spider);
        spiderEngine.setAccessCheckPageUrl("http://checkUrl");
        Mockito.doReturn(authorChangeHandler).when(spiderEngine).getAuthorChangeHandler(Mockito.any(Author.class));
        ipCheckState = createIpCheckState();
        author = new Author();
        author.setName("author");
        author.setUrl(authorUrl);
    }

    @Test
    public void updateAuthor() throws Exception {
        Set<WritingData> writingDatas = new HashSet<>();
        WritingData wd1 = createWritingData("http://w1");
        WritingData wd2 = createWritingData("http://w2");
        WritingData wd3 = createWritingData("http://w3");
        writingDatas.addAll(Arrays.asList(wd1, wd2, wd3));
        Mockito.doReturn(writingDatas).when(spider).readAndParseAuthorsPage(authorUrl);
        spiderEngine.updateAuthor(author);
        Mockito.verify(authorChangeHandler).handleWritingUpdate(Mockito.eq(wd1));
        Mockito.verify(authorChangeHandler).handleWritingUpdate(Mockito.eq(wd2));
        Mockito.verify(authorChangeHandler).handleWritingUpdate(Mockito.eq(wd3));
    }

    @Test
    public void checkServerAccessibilityPositive() throws Exception {
        Mockito.doReturn(ipCheckState).when(spider).readAndParseAccessCheckPage(Mockito.anyString());
        Assert.assertTrue(spiderEngine.checkServerAccessibility());
    }

    @Test
    public void checkServerAccessibilityPositiveNoCheckUrl() throws Exception {
        spiderEngine.setAccessCheckPageUrl(null);
        Assert.assertTrue(spiderEngine.checkServerAccessibility());
        spiderEngine.setAccessCheckPageUrl("");
        Assert.assertTrue(spiderEngine.checkServerAccessibility());
        spiderEngine.setAccessCheckPageUrl("         ");
        Assert.assertTrue(spiderEngine.checkServerAccessibility());
    }

    @Test
    public void checkServerAccessibilityNegative() throws Exception {
        ipCheckState.setBlocked(true);
        Mockito.doReturn(ipCheckState).when(spider).readAndParseAccessCheckPage(Mockito.anyString());
        Assert.assertFalse(spiderEngine.checkServerAccessibility());
    }

    @Test
    public void checkAllAuthorsNotBlocked() throws Exception {
        Mockito.doReturn(true).when(spiderEngine).checkServerAccessibility();
        spiderEngine.checkAllAuthors();
        Mockito.verify(spiderEngine).updateAllAuthors();
    }

    @Test
    public void checkAllAuthorsBlocked() throws Exception {
        Mockito.doReturn(false).when(spiderEngine).checkServerAccessibility();
        spiderEngine.checkAllAuthors();
        Mockito.verify(spiderEngine, Mockito.never()).updateAllAuthors();
    }

    @Test
    public void addAuthorExisting() throws Exception {
        Mockito.doReturn(author).when(authorStorage).getAuthor(authorUrl);
        spiderEngine.addAuthor(authorUrl);
        Mockito.verify(authorStorage, Mockito.never()).saveAuthor(Mockito.any(Author.class));
    }

    @Test
    public void addAuthorNew() throws Exception {
        Mockito.doReturn(null).when(authorStorage).getAuthor(authorUrl);
        spiderEngine.addAuthor(authorUrl);
        Mockito.verify(authorStorage).saveAuthor(Mockito.any(Author.class));
    }

    @Test
    public void removeAuthorNotExists() throws Exception {
        Mockito.doReturn(null).when(authorStorage).getAuthor(authorUrl);
        spiderEngine.removeAuthor(authorUrl);
        Mockito.verify(authorStorage, Mockito.never()).saveAuthor(Mockito.any(Author.class));
    }

    @Test
    public void removeAuthor() throws Exception {
        Mockito.doReturn(author).when(authorStorage).getAuthor(authorUrl);
        spiderEngine.removeAuthor(authorUrl);
        Mockito.verify(authorStorage).delete(Mockito.eq(author));
    }

    private WritingData createWritingData(String url) {
        WritingData writingData = new WritingData();
        writingData.setUrl(url);
        return writingData;
    }

    private IpCheckState createIpCheckState() {
        ipCheckState = new IpCheckState();
        ipCheckState.setBlocked(false);
        ipCheckState.setInSpamList(false);
        ipCheckState.setIp("0.0.0.0");
        ipCheckState.setOtherError(false);
        ipCheckState.setInfo("");
        return ipCheckState;
    }
}