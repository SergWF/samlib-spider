package my.wf.samlib.spider.engine;

import my.wf.samlib.spider.model.Author;
import my.wf.samlib.spider.model.Changes;
import my.wf.samlib.spider.model.Writing;
import my.wf.samlib.spider.model.WritingData;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;


public class AuthorChangeHandlerTest {

    private AuthorChangeHandler authorChangeHandler;
    private Author author;
    private Writing w1;
    private Writing w2;
    private Writing w3;
    private WritingData d1;
    private WritingData d2;
    private WritingData d4;
    private LocalDateTime upd = LocalDateTime.of(2016, 9, 10, 10, 20, 30);
    private LocalDateTime w1Upd = LocalDateTime.of(2016, 9, 1, 10, 20, 30);
    private LocalDateTime w2Upd = LocalDateTime.of(2016, 9, 2, 10, 20, 30);
    private LocalDateTime w3Upd = LocalDateTime.of(2016, 9, 3, 10, 20, 30);


    @Before
    public void setUp() throws Exception {
        author = new Author();
        authorChangeHandler = new AuthorChangeHandler(author, upd);

        author.setUrl("author/");
        w1 = new Writing("url1");
        w1.setSize("10k");
        w1.setName("name1");
        w1.setDescription("descr1");
        w1.setLastUpdated(w1Upd);

        w2 = new Writing("url2");
        w2.setSize("20k");
        w2.setName("name2");
        w2.setDescription("descr2");
        w2.setLastUpdated(w2Upd);

        w3 = new Writing("url3");
        w3.setSize("30k");
        w3.setName("name3");
        w3.setDescription("descr3");
        w3.setLastUpdated(w3Upd);

        author.getWritings().addAll(Arrays.asList(w1, w2, w3));

        d1 = new WritingData();
        d1.setUrl(w1.getUrl());
        d1.setDescription("data descr1");
        d1.setName("data name1");
        d1.setSize("11k");

        d2 = new WritingData();
        d2.setUrl(w2.getUrl());
        d2.setDescription(w2.getDescription());
        d2.setName(w2.getName());
        d2.setSize(w2.getSize());

        d4 = new WritingData();
        d4.setUrl("url4");
        d4.setDescription("data descr4");
        d4.setName("data name4");
        d4.setSize("44k");
    }

    @Test
    public void handleDeleted() throws Exception {
        authorChangeHandler.handleDeleted(author, Arrays.asList(d1, d2, d4));
        Assert.assertThat(author.getWritings(), Matchers.hasSize(2));
        Assert.assertThat(author.getWritings(), Matchers.containsInAnyOrder(w1, w2));
    }

    @Test
    public void handleWritingUpdateNew() throws Exception {
        WritingData data = new WritingData();
        data.setUrl("url4");
        data.setDescription("data descr4");
        data.setName("data name4");
        data.setSize("44k");

        Writing writing = authorChangeHandler.handleWritingUpdate(data);
        Assert.assertThat(writing, Matchers.allOf(Matchers.hasProperty("url", Matchers.is("author/url4")),
                Matchers.hasProperty("name", Matchers.is("data name4")),
                Matchers.hasProperty("size", Matchers.is("44k")),
                Matchers.hasProperty("description", Matchers.is("data descr4")),
                Matchers.hasProperty("lastUpdated", Matchers.is(upd)),
                Matchers.hasProperty("changes", Matchers.contains(Changes.NEW))));
    }

    @Test
    public void handleWritingUpdateUpdated() throws Exception {
        WritingData data = new WritingData();
        data.setUrl("url2");
        data.setDescription("data descr2");
        data.setName("data name2");
        data.setSize("22k");

        Writing writing = authorChangeHandler.handleWritingUpdate(data);
        Assert.assertThat(writing, Matchers.allOf(Matchers.hasProperty("url", Matchers.is("url2")),
                Matchers.hasProperty("name", Matchers.is("data name2")),
                Matchers.hasProperty("size", Matchers.is("22k")),
                Matchers.hasProperty("description", Matchers.is("data descr2")),
                Matchers.hasProperty("lastUpdated", Matchers.is(upd)), Matchers.hasProperty("changes",
                        Matchers.containsInAnyOrder(Changes.NAME, Changes.SIZE, Changes.DESCRIPTION))));
    }

    @Test
    public void handleWritingUpdateNoChanges() throws Exception {
        WritingData data = new WritingData();
        data.setUrl(w2.getUrl());
        data.setDescription(w2.getDescription());
        data.setName(w2.getName());
        data.setSize(w2.getSize());

        Writing writing = authorChangeHandler.handleWritingUpdate(data);
        Assert.assertThat(writing, Matchers.allOf(Matchers.hasProperty("url", Matchers.is("url2")),
                Matchers.hasProperty("lastUpdated", Matchers.is(w2Upd)),
                Matchers.hasProperty("changes", Matchers.empty())));

    }
}