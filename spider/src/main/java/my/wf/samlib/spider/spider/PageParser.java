package my.wf.samlib.spider.spider;

import my.wf.samlib.spider.engine.IpCheckState;
import my.wf.samlib.spider.model.WritingData;

import java.util.Set;

public interface PageParser {
    Set<WritingData> parseAuthorPage(String pageString);
    String parseAuthorName(String pageString);
    IpCheckState parseIpCheckState(String checkPageString);
}
