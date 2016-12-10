package my.wf.samlib.spider.spider;

import my.wf.samlib.spider.engine.IpCheckState;
import my.wf.samlib.spider.model.WritingData;

import java.util.Set;

public interface Spider {
    Set<WritingData> readAndParseAuthorsPage(String url);
    IpCheckState readAndParseAccessCheckPage(String url);
}
