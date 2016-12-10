package my.wf.samlib.spider.spider;

import my.wf.samlib.spider.engine.IpCheckState;
import my.wf.samlib.spider.model.WritingData;

import java.util.Set;

public class SpiderSamlib implements Spider {


    private PageReader pageReader;
    private PageParser pageParser;

    public SpiderSamlib(PageReader pageReader, PageParser pageParser) {
        this.pageReader = pageReader;
        this.pageParser = pageParser;
    }

    @Override
    public Set<WritingData> readAndParseAuthorsPage(String url) {
        String pageString = pageReader.readPageByUrl(url);
        return pageParser.parseAuthorPage(pageString);
    }

    @Override
    public IpCheckState readAndParseAccessCheckPage(String url) {
        String pageString = pageReader.readPageByUrl(url);
        return pageParser.parseIpCheckState(pageString);
    }
}
