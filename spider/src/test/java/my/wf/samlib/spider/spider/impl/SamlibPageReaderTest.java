package my.wf.samlib.spider.spider.impl;

import org.junit.Ignore;
import org.junit.Test;

public class SamlibPageReaderTest {

    @Test
    @Ignore
    public void readPageByUrl() throws Exception {
        String pageByUrl = new SamlibPageReader().readPageByUrl("http://google.com");
        System.out.println(pageByUrl);
    }

}